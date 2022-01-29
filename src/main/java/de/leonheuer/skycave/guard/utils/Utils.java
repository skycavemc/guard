package de.leonheuer.skycave.guard.utils;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.leonheuer.skycave.guard.enums.Message;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;

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

}
