package me.yourname.sicd;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.ShulkerBox;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class SiCD extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPistonPushShulker(BlockPistonExtendEvent event) {
        List<Block> pushedBlocks = event.getBlocks();

        for (Block block : pushedBlocks) {
            if (Tag.SHULKER_BOXES.isTagged(block.getType())) {
                if (block.getState() instanceof ShulkerBox shulkerState) {
                    Material shulkerMaterial = block.getType();

                    ItemStack originalBoxDrop = new ItemStack(shulkerMaterial);
                    BlockStateMeta originalMeta = (BlockStateMeta) originalBoxDrop.getItemMeta();
                    
                    if (originalMeta != null) {
                        originalMeta.setBlockState(shulkerState);
                        originalBoxDrop.setItemMeta(originalMeta);
                    }

                    ItemStack duplicatedBoxDrop = originalBoxDrop.clone();

                    block.getWorld().dropItemNaturally(block.getLocation(), originalBoxDrop);
                    block.getWorld().dropItemNaturally(block.getLocation(), duplicatedBoxDrop);

                    block.setType(Material.AIR);
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }
}
