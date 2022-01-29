package de.leonheuer.skycave.guard.commands;

import de.leonheuer.skycave.guard.Guard;
import de.leonheuer.skycave.guard.enums.Message;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KontrolleCommand implements CommandExecutor, TabCompleter {

    private final Guard main;

    public KontrolleCommand(Guard main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (args.length >= 1) {
            if (sender instanceof Player player) {
                switch (args[0]) {
                    case "now":
                        main.getDataManager().getTimeProfile().setTime(LocalDateTime.now());
                        main.getDataManager().getTimeProfile().setName(player.getName());
                        player.sendMessage(Message.KONTROLLE_SETNOW.getWithPrefix());
                        break;
                    case "spec":
                        if (main.spectators.contains(player)) {
                            main.spectators.remove(player);
                            player.setGameMode(GameMode.SURVIVAL);
                            player.sendMessage(Message.KONTROLLE_SPEC_LEAVE.getWithPrefix());
                        } else {
                            main.spectators.add(player);
                            player.setGameMode(GameMode.SPECTATOR);
                            player.sendMessage(Message.KONTROLLE_SPEC_ENTER.getWithPrefix());
                        }
                        break;
                    default:
                        player.sendMessage(Message.KONTROLLE_WRONGARGS.getWithPrefix());
                }
            } else {
                main.getLogger().severe("Nur Spieler haben Zugriff auf diesen Befehl.");
            }
        } else {
            LocalDateTime time = main.getDataManager().getTimeProfile().getTime();
            String name = main.getDataManager().getTimeProfile().getName();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy, hh:mm:ss");
            if (time != null) {
                long hours = ChronoUnit.HOURS.between(time, LocalDateTime.now());
                time = time.plusHours(hours);
                long minutes = ChronoUnit.MINUTES.between(time, LocalDateTime.now());
                sender.sendMessage(Message.KONTROLLE_TIME.getWithPrefix()
                        .replaceAll("%time", time.format(dtf))
                        .replaceAll("%h", String.valueOf(hours))
                        .replaceAll("%m", String.valueOf(minutes))
                        .replaceAll("%player", name));
            } else {
                sender.sendMessage(Message.KONTROLLE_NODATA.getWithPrefix());
            }
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        List<String> arguments = new ArrayList<>();
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            arguments.add("spec");
            arguments.add("now");
            StringUtil.copyPartialMatches(args[0], arguments, completions);
        }

        Collections.sort(completions);
        return completions;
    }
}
