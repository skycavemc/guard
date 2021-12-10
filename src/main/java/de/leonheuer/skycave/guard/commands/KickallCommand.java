package de.leonheuer.skycave.guard.commands;

import de.leonheuer.skycave.guard.util.Message;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static org.bukkit.Bukkit.getOnlinePlayers;

public class KickallCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String arg, String[] args) {
        if (!(sender instanceof Player)) {
            if (args.length > 0) {
                sender.sendMessage(Message.KICKALL_PROCESS.getWithPrefix());
                for(Player player : getOnlinePlayers()) {
                    if (!(player.hasPermission("skybee.guard.bypass.kickall"))) {
                        player.kickPlayer(ChatColor.translateAlternateColorCodes('&', String.join(" ", args)));
                    }
                }
            } else {
                sender.sendMessage(Message.KICKALL_REASON.getMessage());
            }
        } else {
            sender.sendMessage(Message.CONSOLE_ONLY.getWithPrefix());
        }
        return true;
    }
}
