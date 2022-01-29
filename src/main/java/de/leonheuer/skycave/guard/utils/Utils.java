package de.leonheuer.skycave.guard.utils;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.leonheuer.skycave.guard.enums.Message;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;

import java.time.Duration;

import static org.bukkit.Bukkit.getOnlinePlayers;

public class Utils {

    public static void notifyStaff(Message msg, Player kickedPlayer) {
        for(Player player : getOnlinePlayers()) {
            if (player.hasPermission("skybee.guard.alert.kick")) {
                player.sendMessage(msg.getWithPrefix().replaceAll("%p", kickedPlayer.getName()));
            }
        }
    }

    public static BoundingBox regionToBoundingBox(World world, ProtectedRegion region) {
        BlockVector3 minVector = region.getMinimumPoint();
        Location min = new Location(
                world,
                minVector.getBlockX(),
                minVector.getBlockY(),
                minVector.getBlockZ()
        );
        BlockVector3 maxVector = region.getMinimumPoint();
        Location max = new Location(
                world,
                maxVector.getBlockX(),
                maxVector.getBlockY(),
                maxVector.getBlockZ()
        );
        return BoundingBox.of(min, max);
    }

    public static String replaceLast(String original, String part, String replacement) {
        int pos = original.lastIndexOf(part);
        if (pos > -1) {
            original = original.substring(0, pos) +
                    replacement +
                    original.substring(pos + replacement.length());
        }
        return original;
    }

    public static String formatDuration(Duration duration) {
        String words = DurationFormatUtils.formatDurationWords(duration.toMillis(), true, true);
        words = words.replaceAll("days", "Tagen,");
        words = words.replaceAll("day", "Tag,");
        words = words.replaceAll("hours", "Stunden,");
        words = words.replaceAll("hour", "Stunde,");
        words = words.replaceAll("minutes", "Minuten,");
        words = words.replaceAll("minute", "Minute,");
        words = words.replaceAll("seconds", "Sekunden");
        words = words.replaceAll("second", "Sekunde");
        return replaceLast(words, ",", "und");
    }

}
