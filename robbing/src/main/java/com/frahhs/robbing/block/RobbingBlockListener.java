package com.frahhs.robbing.block;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.RobbingListener;
import com.frahhs.robbing.block.events.RobbingBlockBreakEvent;
import com.frahhs.robbing.block.events.RobbingBlockInteractEvent;
import com.frahhs.robbing.block.events.RobbingBlockPlaceEvent;
import com.frahhs.robbing.item.ItemManager;
import com.frahhs.robbing.item.RobbingItem;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class RobbingBlockListener extends RobbingListener {
    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        ItemManager itemManager = Robbing.getInstance().getItemsManager();
        ItemStack item = e.getItemInHand();

        // Check if is a Robbing item.
        if(!itemManager.isRegistered(item))
            return;

        // Instance of the Robbing item.
        RobbingItem rbItem = itemManager.get(item);

        // Check if is a Robbing block.
        if(!rbItem.getRobbingMaterial().isBlock())
            return;

        // Instance of the Robbing block.
        RobbingBlock block = new RobbingBlock(rbItem, e.getBlock().getLocation());

        // Do the place action.
        block.place(e.getPlayer());

        // RobbingBlockPlaceEvent event.
        RobbingBlockPlaceEvent robbingBlockPlaceEvent = new RobbingBlockPlaceEvent(block, e);

        // Can build option.
        robbingBlockPlaceEvent.setBuild(e.canBuild());

        // call RobbingBlockPlaceEvent event.
        Bukkit.getPluginManager().callEvent(robbingBlockPlaceEvent);
        if(robbingBlockPlaceEvent.isCancelled())
            block.destroy();
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        ItemManager itemManager = Robbing.getInstance().getItemsManager();

        // Check if is a Robbing item
        if(!RobbingBlock.isRobbingBlock(e.getBlock().getLocation()))
            return;

        // Instance of the Robbing block
        RobbingBlock block = RobbingBlock.getFromLocation(e.getBlock().getLocation());

        if(block == null)
            return;

        // Instance of the Robbing item
        RobbingItem item = itemManager.get(block.getRobbingMaterial());

        // Call the RobbingBlockBreakEvent event
        RobbingBlockBreakEvent robbingBlockBreakEvent = new RobbingBlockBreakEvent(block, e);
        Bukkit.getPluginManager().callEvent(robbingBlockBreakEvent);

        // If the RobbingBlockPlaceEvent event is cancelled, cancel the action
        if(robbingBlockBreakEvent.isCancelled()) {
            e.setCancelled(true);
            return;
        }

        e.setDropItems(false);
        // Drop option
        if(robbingBlockBreakEvent.isDropItems() && e.isDropItems() && !e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            e.getPlayer().getWorld().dropItemNaturally(e.getBlock().getLocation(), item.getItemStack());
        }

        // Exp option
        e.setExpToDrop(robbingBlockBreakEvent.getExpToDrop());

        block.destroy();
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if(!e.getEntityType().equals(EntityType.ARMOR_STAND))
            return;

        if(RobbingBlock.isRobbingBlock(e.getEntity()))
            e.setCancelled(true);
    }

    @EventHandler
    public void onManipulate(PlayerArmorStandManipulateEvent e) {
        if(RobbingBlock.isRobbingBlock(e.getRightClicked()))
            e.setCancelled(true);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if(e.getClickedBlock() == null)
            return;

        if(!RobbingBlock.isRobbingBlock(e.getClickedBlock())) {
            return;
        }

        // Call the RobbingBlockBreakEvent event
        RobbingBlock block = RobbingBlock.getFromLocation(e.getClickedBlock().getLocation());
        RobbingBlockInteractEvent robbingBlockBreakEvent = new RobbingBlockInteractEvent(block, e);
        Bukkit.getPluginManager().callEvent(robbingBlockBreakEvent);

        // If the RobbingBlockPlaceEvent event is cancelled, cancel the action
        if(robbingBlockBreakEvent.isCancelled()) {
            e.setCancelled(true);
        }
    }
}
