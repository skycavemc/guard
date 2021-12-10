package de.leonheuer.skycave.guard.listener;

import de.leonheuer.skycave.guard.Guard;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerChangedWorldListener implements Listener {

    private final Guard main;

    public PlayerChangedWorldListener(Guard main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        if (main.getUtil().isSpec(player)) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.setGameMode(GameMode.SPECTATOR);
                }
            }.runTaskLater(main, 2L);
        }
    }

}
