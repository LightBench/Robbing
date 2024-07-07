package com.frahhs.robbing.feature.atm.listener;

import com.frahhs.lightlib.LightListener;
import com.frahhs.lightlib.block.LightBlock;
import com.frahhs.lightlib.block.events.LightBlockBreakEvent;
import com.frahhs.lightlib.block.events.LightBlockInteractEvent;
import com.frahhs.lightlib.block.events.LightBlockPlaceEvent;
import com.frahhs.robbing.feature.atm.item.ATM;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Objects;

public class AtmListener extends LightListener {
    @EventHandler
    public void onPlace(LightBlockPlaceEvent e) {
        if(!(e.getBlockPlaced().getItem() instanceof ATM))
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

        if(!LightBlock.isLightBlock(bottomLocation))
            return;

        LightBlock bottomBlock = LightBlock.getFromLocation(bottomLocation);
        assert bottomBlock != null;
        if(!(bottomBlock.getItem() instanceof ATM))
            return;

        LightBlockInteractEvent robbingBlockInteractEvent = new LightBlockInteractEvent(bottomBlock, e);
        Bukkit.getPluginManager().callEvent(robbingBlockInteractEvent);
    }

    @EventHandler
    public void onBreak(LightBlockBreakEvent e) {
        if(!(e.getBlock().getItem() instanceof ATM))
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

        if(!LightBlock.isLightBlock(bottomLocation))
            return;

        LightBlock bottomBlock = LightBlock.getFromLocation(bottomLocation);
        assert bottomBlock != null;
        if(!(bottomBlock.getItem() instanceof ATM))
            return;

        bottomBlock.destroy();
    }
}
