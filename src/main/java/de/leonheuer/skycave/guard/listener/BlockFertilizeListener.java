package de.leonheuer.skycave.guard.listener;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFertilizeEvent;

public class BlockFertilizeListener implements Listener {

    @EventHandler
    public void onBlockFertilize(BlockFertilizeEvent event) {
        // block moss block spreading
        if (event.getBlock().getType() == Material.MOSS_BLOCK) {
            event.setCancelled(true);
        }
    }

}
