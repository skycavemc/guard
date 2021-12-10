package de.leonheuer.skycave.guard.util;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Bukkit.getOnlinePlayers;

public class Util {

    private final List<Player> spec = new ArrayList<>();

    public void notifyStaff(Message msg, Player kickedPlayer) {
        for(Player player : getOnlinePlayers()) {
            if (player.hasPermission("skybee.guard.alert.kick")) {
                player.sendMessage(msg.getWithPrefix().replaceAll("%p", kickedPlayer.getName()));
            }
        }
    }

    public void addSpec(Player player) {
        spec.add(player);
    }

    public void removeSpec(Player player) {
        spec.remove(player);
    }

    public boolean isSpec(Player player) {
        return spec.contains(player);
    }

}
