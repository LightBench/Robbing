package com.frahhs.robbing.feature.atm.listener;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.RobbingListener;
import com.frahhs.robbing.block.RobbingBlock;
import com.frahhs.robbing.block.events.RobbingBlockBreakEvent;
import com.frahhs.robbing.block.events.RobbingBlockInteractEvent;
import com.frahhs.robbing.block.events.RobbingBlockPlaceEvent;
import com.frahhs.robbing.item.RobbingMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class AtmListener extends RobbingListener {
    @EventHandler
    public void onPlace(RobbingBlockPlaceEvent e) {
        if(!e.getBlockPlaced().getRobbingMaterial().equals(RobbingMaterial.ATM))
            return;

        Location topLocation = e.getBlock().getLocation().add(0, 1, 0);
        Block topBlock = Objects.requireNonNull(topLocation.getWorld()).getBlockAt(topLocation);

        if(!topBlock.getType().equals(Material.AIR)) {
            e.setCancelled(true);
            //TODO: add to messages
            String message = "Atm need 2 block to be placed, the space on top is occupated.";
            e.getPlayer().sendMessage(message);
            return;
        }

        topBlock.setType(Material.BARRIER);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if(!Objects.equals(e.getHand(), EquipmentSlot.HAND))
            return;

        if(e.getClickedBlock() == null)
            return;

        Location bottomLocation = e.getClickedBlock().getLocation().add(0, -1, 0);

        if(!RobbingBlock.isRobbingBlock(bottomLocation))
            return;

        RobbingBlock bottomBlock = RobbingBlock.getFromLocation(bottomLocation);
        assert bottomBlock != null;
        if(!bottomBlock.getRobbingMaterial().equals(RobbingMaterial.ATM))
            return;

        RobbingBlockInteractEvent robbingBlockInteractEvent = new RobbingBlockInteractEvent(bottomBlock, e);
        Bukkit.getPluginManager().callEvent(robbingBlockInteractEvent);
    }

    @EventHandler
    public void onBreak(RobbingBlockBreakEvent e) {
        if(!e.getBlock().getRobbingMaterial().equals(RobbingMaterial.ATM))
            return;

        Location topLocation = e.getBlock().getLocation().clone().add(0, 1, 0);
        Block topBlock = Objects.requireNonNull(topLocation.getWorld()).getBlockAt(topLocation);

        if(!topBlock.getType().equals(Material.BARRIER)) {
            return;
        }

        topBlock.setType(Material.AIR);
    }

    @EventHandler
    public void onTopBreak(BlockBreakEvent e) {
        if(!e.getBlock().getType().equals(Material.BARRIER))
            return;

        Location bottomLocation = e.getBlock().getLocation().add(0, -1, 0);

        if(!RobbingBlock.isRobbingBlock(bottomLocation))
            return;

        RobbingBlock bottomBlock = RobbingBlock.getFromLocation(bottomLocation);
        assert bottomBlock != null;
        if(!bottomBlock.getRobbingMaterial().equals(RobbingMaterial.ATM))
            return;

        bottomBlock.destroy();
    }
}
