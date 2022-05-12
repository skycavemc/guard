package de.leonheuer.skycave.guard.commands;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.leonheuer.skycave.guard.enums.Message;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.*;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CountEntityCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 0) {
            sender.sendMessage(Message.COUNTENTITY_MISSINGARG.getWithPrefix());
            return true;
        }

        if (args[0].startsWith("-w:")) {
            String[] extracted = args[0].split(":");
            World world = Bukkit.getWorld(extracted[1]);
            if (world == null) {
                sender.sendMessage(Message.COUNTENTITY_REGION_INVALID_WORLD.getWithPrefix()
                        .replaceAll("%world", extracted[1])
                );
                return true;
            }

            if (args.length >= 2) {
                RegionManager rm = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world));
                if (rm == null) {
                    return true;
                }
                ProtectedRegion rg = rm.getRegion(args[1]);
                if (rg == null) {
                    sender.sendMessage(Message.COUNTENTITY_REGION_INVALID.getWithPrefix()
                            .replaceAll("%region", args[1])
                    );
                    return true;
                }

                if (args.length >= 3) {
                    EntityType type;
                    try {
                        type = EntityType.valueOf(args[2].toUpperCase());
                    } catch (IllegalArgumentException e) {
                        sender.sendMessage(Message.COUNTENTITY_INVALID.getWithPrefix()
                                .replaceAll("%entity", args[2]));
                        return true;
                    }

                    int count = 0;
                    for (Entity e : world.getEntities()) {
                        if (e.getType() != type) {
                            continue;
                        }
                        if (rg.contains(BukkitAdapter.asBlockVector(e.getLocation()))) count++;
                    }
                    sender.sendMessage(Message.COUNTENTITY_REGION_HEADER.getWithPrefix().replaceAll("%region", rg.getId()));
                    sender.sendMessage(Message.COUNTENTITY_REGION_OUTPUT.getMessage()
                            .replaceAll("%entity", type.toString())
                            .replaceAll("%count", String.valueOf(count))
                    );
                    return true;
                }

                int villager = 0;
                int living = 0;
                int item = 0;
                int misc = 0;

                for (Entity e : world.getEntities()) {
                    if (rg.contains(BukkitAdapter.asBlockVector(e.getLocation()))) {
                        if (e instanceof Villager) {
                            villager++;
                            continue;
                        }
                        if (e instanceof LivingEntity) {
                            living++;
                            continue;
                        }
                        if (e instanceof Item) {
                            item++;
                            continue;
                        }
                        misc++;
                    }
                }
                sender.sendMessage(Message.COUNTENTITY_REGION_HEADER.getWithPrefix().replaceAll("%region", rg.getId()));
                sender.sendMessage(Message.COUNTENTITY_REGION_OUTPUT.getMessage()
                        .replaceAll("%entity", "Villager")
                        .replaceAll("%count", String.valueOf(villager))
                );
                sender.sendMessage(Message.COUNTENTITY_REGION_OUTPUT.getMessage()
                        .replaceAll("%entity", "Lebewesen")
                        .replaceAll("%count", String.valueOf(living))
                );
                sender.sendMessage(Message.COUNTENTITY_REGION_OUTPUT.getMessage()
                        .replaceAll("%entity", "Items")
                        .replaceAll("%count", String.valueOf(item))
                );
                sender.sendMessage(Message.COUNTENTITY_REGION_OUTPUT.getMessage()
                        .replaceAll("%entity", "Sonstiges")
                        .replaceAll("%count", String.valueOf(misc))
                );
            }
            return true;
        }

        EntityType type;
        try {
            type = EntityType.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            sender.sendMessage(Message.COUNTENTITY_INVALID.getWithPrefix()
                    .replaceAll("%entity", args[0]));
            return true;
        }
        sender.sendMessage(Message.COUNTENTITY_HEADER.getWithPrefix()
                .replaceAll("%entity", StringUtils.capitalize(args[0]))
        );
        for (World world : Bukkit.getWorlds()) {
            long count = world.getEntities().stream()
                    .filter(entity -> entity.getType() == type)
                    .count();
            sender.sendMessage(Message.COUNTENTITY_OUTPUT.getMessage()
                    .replaceAll("%world", world.getName())
                    .replaceAll("%count", String.valueOf(count)));
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        List<String> arguments = new ArrayList<>();
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            Arrays.stream(EntityType.values()).forEach(type -> arguments.add(type.toString().toLowerCase()));
            Bukkit.getWorlds().forEach(world -> arguments.add("-w:" + world.getName()));
            StringUtil.copyPartialMatches(args[0], arguments, completions);
        }

        Collections.sort(completions);
        return completions;
    }

}
