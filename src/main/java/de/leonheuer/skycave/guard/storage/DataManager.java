package de.leonheuer.skycave.guard.storage;

import de.leonheuer.skycave.guard.Guard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class DataManager {

    private final Guard main;
    private final String path = "plugins/SkyBeeGuard/playerdata/";
    private final String path2 = "plugins/SkyBeeGuard/";
    private final HashMap<UUID, PlayerProfile> profiles = new HashMap<>();
    private final HashMap<UUID, String> ipmap = new HashMap<>();
    private final TimeProfile timeProfile;

    public DataManager(Guard main) {
        this.main = main;
        File dir = new File(path);
        JSONParser jsonParser = new JSONParser();
        if (dir.exists()) {
            if (dir.listFiles() != null) {
                for (File file : dir.listFiles()) {
                    String[] fileName = file.getName().split("\\.");
                    UUID uuid = UUID.fromString(fileName[0]);
                    try (FileReader reader = new FileReader(file)) {
                        JSONObject data = (JSONObject) jsonParser.parse(reader);
                        ipmap.put(uuid, (String) data.get("ip"));
                    } catch (IOException | ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            dir.mkdirs();
        }
        File file = new File(path2 + "lastcontol.json");
        timeProfile = new TimeProfile(this, null, null);
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
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void readPlayerData(Player player) {
        UUID uuid = player.getUniqueId();
        PlayerProfile profile = new PlayerProfile(0, uuid, player.getAddress().getAddress(), this);
        File file = new File(path, uuid.toString() + ".json");
        if (file.exists()) {
            JSONParser jsonParser = new JSONParser();
            try (FileReader reader = new FileReader(file)) {
                JSONObject data = (JSONObject) jsonParser.parse(reader);
                profile.setAfk(Math.toIntExact((long) data.get("afk")));
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
        ipmap.put(uuid, player.getAddress().getAddress().toString());
        profiles.put(uuid, profile);
    }
    
    public void writePlayerData(@NotNull PlayerProfile profile) {
        File file = new File(path, profile.getUuid().toString() + ".json");
        JSONObject data = new JSONObject();

        if (!file.exists()) {
            try {
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

    public PlayerProfile getPlayerProfile(UUID uuid) {
        if (!profiles.containsKey(uuid)) {
            readPlayerData(Bukkit.getPlayer(uuid));
        }
        return profiles.get(uuid);
    }

    public PlayerProfile getPlayerProfile(Player player) {
        return profiles.getOrDefault(player.getUniqueId(), new PlayerProfile(0, player.getUniqueId(), player.getAddress().getAddress(), this));
    }

    public List<UUID> getIPMatches(UUID initial, String ip) {
        List<UUID> matches = new ArrayList<>();
        for (UUID uuid : ipmap.keySet()) {
            if (!uuid.toString().equalsIgnoreCase(initial.toString()) && ipmap.get(uuid).equals(ip)) {
                matches.add(uuid);
            }
        }
        return matches;
    }

    public String getOfflineIP(UUID uuid) {
        return ipmap.getOrDefault(uuid, null);
    }

    public TimeProfile getTimeProfile() {
        return timeProfile;
    }

    public void saveTimeProfile() {
        File file = new File(path2, "lastcontol.json");
        JSONObject data = new JSONObject();

        if (!file.exists()) {
            try {
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
