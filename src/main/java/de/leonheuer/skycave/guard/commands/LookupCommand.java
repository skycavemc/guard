package de.leonheuer.skycave.guard.commands;

import de.leonheuer.skycave.guard.Guard;
import de.leonheuer.skycave.guard.enums.Message;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;

public class LookupCommand implements CommandExecutor {

    private final Guard main;

    public LookupCommand(Guard main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Message.LOOKUP_MISSING.getWithPrefix());
            return true;
        }

        Player other = Bukkit.getPlayer(args[0]);
        StringJoiner sj = new StringJoiner("§8, §7");

        if (other == null || !other.isOnline()) {
            OfflinePlayer otherOffline = Bukkit.getOfflinePlayerIfCached(args[0]);
            if (otherOffline == null) {
                sender.sendMessage(Message.LOOKUP_UNKNOWN.getWithPrefix().replaceAll("%player", args[0]));
                return true;
            }

            UUID otherUuid = otherOffline.getUniqueId();
            String ip = main.getDataManager().getOfflineIP(otherUuid);
            if (ip == null) {
                sender.sendMessage(Message.LOOKUP_IP_NOT_FOUND.getWithPrefix()
                        .replaceAll("%player", args[0]));
                return true;
            }

            List<UUID> matches = main.getDataManager()
                    .whoJoinedWithIP(otherUuid, ip);
            for (UUID u : matches) {
                OfflinePlayer player = Bukkit.getOfflinePlayer(u);
                sj.add(player.getName());
            }

            if (sj.length() == 0) {
                sender.sendMessage(Message.LOOKUP_ACCOUNTS.getWithPrefix()
                        .replaceAll("%player", args[0])
                        .replaceAll("%accounts", "§ckeine")
                );
            } else {
                sender.sendMessage(Message.LOOKUP_ACCOUNTS.getWithPrefix()
                        .replaceAll("%player", args[0])
                        .replaceAll("%accounts", sj.toString())
                );
            }
            return true;
        }

        InetSocketAddress address = other.getAddress();
        if (address == null || address.getAddress() == null) {
            sender.sendMessage(Message.LOOKUP_IP_NOT_FOUND.getWithPrefix()
                    .replaceAll("%player", other.getName()));
            return true;
        }

        List<UUID> matches = main.getDataManager()
                .whoJoinedWithIP(other.getUniqueId(), address.getAddress().toString());
        for (UUID u : matches) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(u);
            sj.add(player.getName());
        }

        if (sj.length() == 0) {
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
        return true;
    }

}
