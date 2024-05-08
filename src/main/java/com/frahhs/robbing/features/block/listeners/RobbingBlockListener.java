package com.frahhs.robbing.features.block.listeners;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.features.BaseListener;
import com.frahhs.robbing.features.block.events.RobbingBlockBreakEvent;
import com.frahhs.robbing.features.block.events.RobbingBlockPlaceEvent;
import com.frahhs.robbing.item.ItemsManager;
import com.frahhs.robbing.item.RobbingBlock;
import com.frahhs.robbing.item.RobbingItem;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class RobbingBlockListener extends BaseListener {

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        ItemsManager itemsManager = Robbing.getInstance().getItemsManager();
        ItemStack item = e.getItemInHand();

        // Check if is a Robbing item
        if(!itemsManager.isRegistered(item))
            return;

        // Instance of the Robbing item
        RobbingItem rbItem = itemsManager.getByItemStack(item);

        // Check if is a Robbing block
        if(!rbItem.getRBMaterial().isBlock())
            return;

        // Instance of the Robbing block
        RobbingBlock block = new RobbingBlock(rbItem, e.getBlock().getLocation());

        // Call the RobbingBlockPlaceEvent event
        RobbingBlockPlaceEvent robbingBlockPlaceEvent = new RobbingBlockPlaceEvent(block, e);
        Bukkit.getPluginManager().callEvent(robbingBlockPlaceEvent);

        // If the RobbingBlockPlaceEvent event is cancelled, cancel the action
        if(robbingBlockPlaceEvent.isCancelled()) {
            e.setCancelled(true);
            return;
        }

        // Can build option
        robbingBlockPlaceEvent.setBuild(e.canBuild());

        block.save();
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        ItemsManager itemsManager = Robbing.getInstance().getItemsManager();

        // Check if is a Robbing item
        if(!RobbingBlock.isRBBlock(e.getBlock().getLocation()))
            return;

        // Instance of the Robbing block
        RobbingBlock block = RobbingBlock.getFromLocation(e.getBlock().getLocation());

        if(block == null)
            return;

        // Instance of the Robbing item
        RobbingItem item = itemsManager.get(block.getItem().getRBMaterial());

        // Call the RobbingBlockBreakEvent event
        RobbingBlockBreakEvent robbingBlockBreakEvent = new RobbingBlockBreakEvent(block, e);
        Bukkit.getPluginManager().callEvent(robbingBlockBreakEvent);

        // If the RobbingBlockPlaceEvent event is cancelled, cancel the action
        if(robbingBlockBreakEvent.isCancelled()) {
            e.setCancelled(true);
            return;
        }

        // Drop option
        if(robbingBlockBreakEvent.isDropItems() && e.isDropItems() && !e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            e.setDropItems(false);
            e.getPlayer().getWorld().dropItemNaturally(e.getBlock().getLocation(), item.getItemStack());
        }

        // Exp option
        e.setExpToDrop(robbingBlockBreakEvent.getExpToDrop());

        block.remove();
    }
}
