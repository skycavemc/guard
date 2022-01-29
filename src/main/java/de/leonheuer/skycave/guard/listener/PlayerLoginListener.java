package de.leonheuer.skycave.guard.listener;

import de.leonheuer.skycave.guard.Guard;
import de.leonheuer.skycave.guard.enums.Message;
import de.leonheuer.skycave.guard.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerLoginListener implements Listener {

    private final Guard main;
    private Long lastAntiBotCheck;
    private int joinedAntiBotCheck;

    public PlayerLoginListener(Guard main) {
        this.main = main;
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();

        if (event.getResult() == PlayerLoginEvent.Result.KICK_FULL) {
            if (player.hasPermission("skybee.guard.bypass.full")) {
                event.allow();
            } else {
                event.setKickMessage(Message.KICK_FULL.getMessage());
                Utils.notifyStaff(Message.NOTIFIER_FULL_SERVER.getWithPrefix().replaceAll("%p", player.getName()));
            }
        } else if (event.getResult() == PlayerLoginEvent.Result.KICK_WHITELIST) {
            if (player.hasPermission("skybee.guard.bypass.whitelist")) {
                event.allow();
            } else {
                event.setKickMessage(Message.KICK_WHITELIST.getMessage());
                Utils.notifyStaff(Message.NOTIFIER_WHITELIST.getWithPrefix().replaceAll("%p", player.getName()));
            }
        } else {
            if (lastAntiBotCheck == null) {
                lastAntiBotCheck = System.currentTimeMillis();
            }

            long now = System.currentTimeMillis();
            long antiBotCheckTimeDiff = (now - lastAntiBotCheck) / 1000;

            if (antiBotCheckTimeDiff > 10) {
                lastAntiBotCheck = System.currentTimeMillis();
                joinedAntiBotCheck = 1;
            } else {
                joinedAntiBotCheck = joinedAntiBotCheck + 1;
                if (joinedAntiBotCheck > 4) {
                    if (!(player.hasPermission("skybee.guard.bypass.antibot"))) {
                        event.disallow(PlayerLoginEvent.Result.KICK_OTHER, Message.KICK_ANTIBOT.getMessage());
                        Utils.notifyStaff(Message.NOTIFIER_ANTIBOT.getWithPrefix().replaceAll("%p", player.getName()));
                    }
                }
            }
        }
    }

}
