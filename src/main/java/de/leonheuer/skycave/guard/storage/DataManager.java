package de.leonheuer.skycave.guard.storage;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.util.*;

public class DataManager {

    private final String path = "plugins/SkyBeeGuard/playerdata/";
    private final String path2 = "plugins/SkyBeeGuard/";
    private final HashMap<UUID, PlayerProfile> profiles = new HashMap<>();
    private final HashMap<UUID, String> ipMap = new HashMap<>();
    private final TimeProfile timeProfile;

    public DataManager() {
        File dir = new File(path);
        JSONParser jsonParser = new JSONParser();
        if (dir.exists()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    String[] fileName = file.getName().split("\\.");
                    UUID uuid = UUID.fromString(fileName[0]);
                    try (FileReader reader = new FileReader(file)) {
                        JSONObject data = (JSONObject) jsonParser.parse(reader);
                        ipMap.put(uuid, (String) data.get("ip"));
                    } catch (IOException | ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            //noinspection ResultOfMethodCallIgnored
            dir.mkdirs();
        }
        File file = new File(path2 + "lastcontol.json");
        timeProfile = new TimeProfile(null, null);
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                JSONObject data = (JSONObject) jsonParser.parse(reader);
                timeProfile.setTime(LocalDateTime.parse((String) data.get("time")));
                timeProfile.setName((String) data.get("name"));
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        } else {
            try {
                //noinspection ResultOfMethodCallIgnored
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void readPlayerData(Player player) {
        UUID uuid = player.getUniqueId();
        InetSocketAddress address = player.getAddress();
        InetAddress ip;
        if (address == null || address.getAddress() == null) {
            ip = null;
        } else {
            ip = address.getAddress();
            ipMap.put(uuid, ip.toString());
        }
        PlayerProfile profile = new PlayerProfile(0, uuid, ip, this);
        File file = new File(path, uuid + ".json");
        if (file.exists()) {
            JSONParser jsonParser = new JSONParser();
            try (FileReader reader = new FileReader(file)) {
                JSONObject data = (JSONObject) jsonParser.parse(reader);
                profile.setAfk(Math.toIntExact((long) data.get("afk")));
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
        profiles.put(uuid, profile);
    }
    
    public void writePlayerData(@NotNull PlayerProfile profile) {
        File file = new File(path, profile.getUuid().toString() + ".json");
        JSONObject data = new JSONObject();

        if (!file.exists()) {
            try {
                //noinspection ResultOfMethodCallIgnored
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        data.put("afk", profile.getAfk());
        data.put("ip", profile.getIp().toString());

        try (FileWriter writer = new FileWriter(path + profile.getUuid().toString() + ".json")) {
            writer.write(data.toJSONString());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void unloadPlayerData(UUID uuid) {
        profiles.remove(uuid);
    }

    @Nullable
    public PlayerProfile getPlayerProfile(Player player) {
        if (!profiles.containsKey(player.getUniqueId())) {
            readPlayerData(player);
        }
        return profiles.getOrDefault(player.getUniqueId(), null);
    }

    /**
     * Gets a list of all UUIDs belonging to players who have joined the server under the specified IP address.
     * @param exclude The uuid of the player to exclude
     * @param ip The IP address (as a String) to get all matching UUIDs for
     * @return The list of matching UUIDs
     */
    public List<UUID> whoJoinedWithIP(UUID exclude, @NotNull String ip) {
        List<UUID> matches = new ArrayList<>();
        for (Map.Entry<UUID, String> entry : ipMap.entrySet()) {
            if (entry.getKey() != exclude && entry.getValue().equals(ip)) {
                matches.add(entry.getKey());
            }
        }
        return matches;
    }

    @Nullable
    public String getOfflineIP(UUID uuid) {
        return ipMap.getOrDefault(uuid, null);
    }

    public TimeProfile getTimeProfile() {
        return timeProfile;
    }

    public void saveTimeProfile() {
        File file = new File(path2, "lastcontol.json");
        JSONObject data = new JSONObject();

        if (!file.exists()) {
            try {
                //noinspection ResultOfMethodCallIgnored
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        LocalDateTime time = timeProfile.getTime();
        String name = timeProfile.getName();
        if (time != null && name != null) {
            data.put("time", time.toString());
            data.put("name", name);
        }

        try (FileWriter writer = new FileWriter(path2 + "lastcontol.json")) {
            writer.write(data.toJSONString());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
