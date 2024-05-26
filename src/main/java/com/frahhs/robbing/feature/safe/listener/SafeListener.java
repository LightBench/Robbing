package com.frahhs.robbing.feature.safe.listener;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.RobbingListener;
import com.frahhs.robbing.block.events.RobbingBlockBreakEvent;
import com.frahhs.robbing.block.events.RobbingBlockInteractEvent;
import com.frahhs.robbing.block.events.RobbingBlockPlaceEvent;
import com.frahhs.robbing.feature.safe.SafeInventory;
import com.frahhs.robbing.feature.safe.bag.SafeInventoryBag;
import com.frahhs.robbing.feature.safe.mcp.SafeController;
import com.frahhs.robbing.item.RobbingMaterial;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.EquipmentSlot;

public class SafeListener extends RobbingListener {
    private final SafeController safeController;

    public SafeListener() {
        safeController = new SafeController();
    }

    @EventHandler
    public void onOpen(RobbingBlockInteractEvent e) {
        if(!e.getBlock().getRobbingMaterial().equals(RobbingMaterial.SAFE))
            return;

        if(e.getPlayer().isSneaking() && !( e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.AIR)))
            return;

        assert e.getHand() != null;
        if(!e.getHand().equals(EquipmentSlot.HAND))
            return;

        if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            return;

        safeController.openGUI(e.getBlock(), e.getPlayer());
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if(!(e.getInventory().getHolder() instanceof SafeInventory))
            return;

        SafeInventory safeInventory = (SafeInventory) e.getInventory().getHolder();
        safeController.update(safeInventory.getSafe(), e.getInventory());
    }

    @EventHandler
    public void onPlace(RobbingBlockPlaceEvent e) {
        if(!e.getBlock().getRobbingMaterial().equals(RobbingMaterial.SAFE))
            return;

        safeController.placeBlock(e.getBlock(), e.getItemInHand());
    }

    @EventHandler
    public void onBreak(RobbingBlockBreakEvent e) {
        if(!e.getBlock().getRobbingMaterial().equals(RobbingMaterial.SAFE))
            return;

        // Drop option
        if(!e.isCancelled() && e.isDropItems() && !e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            e.setDropItems(false);

            // TODO: not to be here, but in a provider.
            SafeInventoryBag safeInventoryBag = (SafeInventoryBag) Robbing.getInstance().getBagManager().getBag("SafeInventoryBag");
            safeInventoryBag.getData().remove(e.getBlock().getArmorStand().getUniqueId());

            safeController.dropBlock(e.getBlock(), e.getPlayer());
        }
    }

    @EventHandler
    public void onOtherPlace(BlockPlaceEvent e) {
        if(e.getPlayer().getOpenInventory().getTopInventory().getHolder() instanceof SafeInventory)
            e.setCancelled(true);
    }
}
