package de.leonheuer.skycave.guard.commands;

import de.leonheuer.skycave.guard.enums.Message;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.StringJoiner;

public class GCCommand implements CommandExecutor {

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        StringJoiner sj = new StringJoiner(", ");
        DecimalFormat format = new DecimalFormat("0.00");

        for (double tps : Bukkit.getTPS()) {
            if (tps < 10.0D) {
                sj.add("§4" + format.format(tps));
            } else if (tps < 16.0D) {
                sj.add("§c" + format.format(tps));
            } else if (tps < 18.0D) {
                sj.add("§e" + format.format(tps));
            } else {
                sj.add("§a" + format.format(tps));
            }
        }

        sender.sendMessage(Message.GC_TPS.getMessage().replaceAll("%tps", sj.toString()));

        double used = (double)(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1.0E9D;
        double total = (double)Runtime.getRuntime().totalMemory() / 1.0E9D;
        double rawRam = used / total * 100.0D;
        String ram;
        if (rawRam > 95.0D) {
            ram = "§4" + (new DecimalFormat("0.00")).format(rawRam);
        } else if (rawRam > 85.0D) {
            ram = "§c" + (new DecimalFormat("0.00")).format(rawRam);
        } else if (rawRam > 65.0D) {
            ram = "§e" + (new DecimalFormat("0.00")).format(rawRam);
        } else {
            ram = "§a" + (new DecimalFormat("0.00")).format(rawRam);
        }

        sender.sendMessage(Message.GC_RAM.getMessage().replaceAll("%ram", ram));
        sender.sendMessage(Message.GC_WORLDS_HEADER.getMessage());

        for (World world : Bukkit.getWorlds()) {
            sender.sendMessage(Message.GC_WORLDS.getMessage()
                    .replaceAll("%world", world.getName())
                    .replaceAll("%entity", String.valueOf(world.getEntityCount()))
                    .replaceAll("%tile", String.valueOf(world.getTileEntityCount()))
                    .replaceAll("%players", String.valueOf(world.getPlayerCount()))
                    .replaceAll("%chunks", String.valueOf(world.getLoadedChunks().length))
            );
        }
        return true;
    }

}
