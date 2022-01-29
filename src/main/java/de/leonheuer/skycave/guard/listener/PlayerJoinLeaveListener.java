package de.leonheuer.skycave.guard.listener;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.leonheuer.skycave.guard.Guard;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinLeaveListener implements Listener {

    private final Guard main;

    public PlayerJoinLeaveListener(Guard main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        main.getDataManager().readPlayerData(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        main.getDataManager().unloadPlayerData(event.getPlayer().getUniqueId());
        main.spectators.remove(event.getPlayer());
    }

    @SuppressWarnings({"UnstableApiUsage", "deprecation"})
    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        event.setCancelled(true);

        main.getDataManager().unloadPlayerData(event.getPlayer().getUniqueId());
        main.spectators.remove(event.getPlayer());

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF("lobby");
        event.getPlayer().sendPluginMessage(main, "BungeeCord", out.toByteArray());

        out = ByteStreams.newDataOutput();
        out.writeUTF("Message");
        out.writeUTF(event.getPlayer().getName());
        out.writeUTF("§e§l| §6Lobby §8» §cDu wurdest von SkyBlock gekickt! Grund: §e" + event.getReason());
        event.getPlayer().sendPluginMessage(main, "BungeeCord", out.toByteArray());
    }

}
