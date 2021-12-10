package de.leonheuer.skycave.guard.commands;

import de.leonheuer.skycave.guard.util.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import static org.bukkit.Bukkit.getOnlinePlayers;

public class NotifyCommand implements Listener, CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String arg, String[] args) {
        if (!(sender instanceof Player)) {
            if (args.length == 4) {
                for(Player player : getOnlinePlayers()) {
                    if (player.hasPermission("skybee.guard.alert.aacn")) {
                        player.sendMessage(Message.NOTIFY_AAC.getMessage()
                                .replaceAll("%1", args[0])
                                .replaceAll("%2", args[1])
                                .replaceAll("%3", args[2])
                                .replaceAll("%4", args[3])
                        );
                    }
                }
            } else if (args.length == 5) {
                for(Player player : getOnlinePlayers()) {
                    if (player.hasPermission("skybee.guard.alert.aacn")) {
                        player.sendMessage(Message.NOTIFY_AAC_VL.getMessage()
                                .replaceAll("%1", args[0])
                                .replaceAll("%2", args[1])
                                .replaceAll("%3", args[2])
                                .replaceAll("%4", args[3])
                                .replaceAll("%5", args[4])
                        );
                    }
                }
            } else {
                sender.sendMessage(Message.NOTIFY_MISSING_ARGS.getMessage()
                        .replaceAll("%0", String.valueOf(args.length))
                        .replaceAll("%1", "4/5")
                );
            }
        } else {
            sender.sendMessage(Message.UNKNOWN_COMMAND.getMessage());
        }
        return true;
    }

}
