package de.leonheuer.skycave.guard.commands;

import de.leonheuer.skycave.guard.Guard;
import de.leonheuer.skycave.guard.util.Message;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.StringJoiner;
import java.util.UUID;

public class LookupCommand implements CommandExecutor {

    private final Guard main;

    public LookupCommand(Guard main) {
        this.main = main;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (args.length >= 1) {
            Player other = Bukkit.getPlayer(args[0]);
            StringJoiner sj = new StringJoiner("§8, §7");
            if (other == null) {
                OfflinePlayer otherOffline = Bukkit.getOfflinePlayer(args[0]);
                if (otherOffline != null) {
                    try {
                        UUID offlineUuid = otherOffline.getUniqueId();
                        main.getDm().getIPMatches(offlineUuid, main.getDm().getOfflineIP(offlineUuid)).forEach(
                                uuid -> sj.add(Bukkit.getOfflinePlayer(uuid).getName())
                        );
                        if (sj.toString().equals("")) {
                            sender.sendMessage(Message.LOOKUP_ACCOUNTS.getWithPrefix()
                                    .replaceAll("%player", otherOffline.getName())
                                    .replaceAll("%accounts", "§ckeine")
                            );
                        } else {
                            sender.sendMessage(Message.LOOKUP_ACCOUNTS.getWithPrefix()
                                    .replaceAll("%player", otherOffline.getName())
                                    .replaceAll("%accounts", sj.toString())
                            );
                        }
                    } catch (NullPointerException e) {
                        sender.sendMessage(Message.LOOKUP_UNKNOWN.getWithPrefix().replaceAll("%player", args[0]));
                    }
                } else {
                    sender.sendMessage(Message.LOOKUP_UNKNOWN.getWithPrefix().replaceAll("%player", args[0]));
                }
            } else {
                main.getDm().getIPMatches(other.getUniqueId(), other.getAddress().getAddress().toString()).forEach(
                        uuid -> sj.add(Bukkit.getOfflinePlayer(uuid).getName())
                );
                if (sj.toString().equals("")) {
                    sender.sendMessage(Message.LOOKUP_ACCOUNTS.getWithPrefix()
                            .replaceAll("%player", other.getName())
                            .replaceAll("%accounts", "§ckeine")
                    );
                } else {
                    sender.sendMessage(Message.LOOKUP_ACCOUNTS.getWithPrefix()
                            .replaceAll("%player", other.getName())
                            .replaceAll("%accounts", sj.toString())
                    );
                }
            }
        } else {
            sender.sendMessage(Message.LOOKUP_MISSING.getWithPrefix());
        }
        return true;
    }

}
