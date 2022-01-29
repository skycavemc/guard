package de.leonheuer.skycave.guard.utils;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.leonheuer.skycave.guard.enums.Message;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;

import java.time.Duration;

import static org.bukkit.Bukkit.getOnlinePlayers;

public class Utils {

    public static void notifyStaff(String message) {
        for(Player player : getOnlinePlayers()) {
            if (player.hasPermission("skybee.guard.alert.kick")) {
                player.sendMessage(message);
            }
        }
    }

    public static String replaceLast(String original, String part, String replacement) {
        int pos = original.lastIndexOf(part);
        if (pos > -1) {
            original = original.substring(0, pos) +
                    replacement +
                    original.substring(pos + part.length());
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
        return replaceLast(words, ",", " und");
    }

}
