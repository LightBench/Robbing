package com.frahhs.robbing.feature.safe.listener;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.RobbingListener;
import com.frahhs.robbing.block.events.RobbingBlockBreakEvent;
import com.frahhs.robbing.block.events.RobbingBlockInteractEvent;
import com.frahhs.robbing.block.events.RobbingBlockPlaceEvent;
import com.frahhs.robbing.feature.safe.mcp.SafeInventory;
import com.frahhs.robbing.feature.safe.mcp.SafeController;
import com.frahhs.robbing.feature.safe.mcp.SafeModel;
import com.frahhs.robbing.item.ItemManager;
import com.frahhs.robbing.item.RobbingMaterial;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class SafeListener extends RobbingListener {
    private final SafeController safeController;
    private final ItemManager itemManager;

    public SafeListener() {
        safeController = new SafeController();
        itemManager = Robbing.getInstance().getItemsManager();
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

        ItemStack itemInMainHand = e.getPlayer().getInventory().getItemInMainHand();
        ItemStack lockpickItem = itemManager.get(RobbingMaterial.LOCKPICK).getItemStack();
        if(SafeModel.isLocked(e.getBlock()) && itemInMainHand.isSimilar(lockpickItem))
            return;

        safeController.open(e.getBlock(), e.getPlayer());
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

        if(!e.isCancelled())
            safeController.placeBlock(e.getBlock(), e.getItemInHand());
    }

    @EventHandler
    public void onBreak(RobbingBlockBreakEvent e) {
        if(!e.getBlock().getRobbingMaterial().equals(RobbingMaterial.SAFE))
            return;

        // Drop option
        if(!e.isCancelled() && e.isDropItems() && !e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            e.setDropItems(false);

            // If the safe is not locked drop all the content.
            if(!SafeModel.isLocked(e.getBlock()))
                safeController.dropInventory(e.getBlock(), e.getPlayer());

            safeController.dropBlock(e.getBlock(), e.getPlayer());
        }
    }

    @EventHandler
    public void onOtherPlace(BlockPlaceEvent e) {
        if(e.getPlayer().getOpenInventory().getTopInventory().getHolder() instanceof SafeInventory)
            e.setCancelled(true);
    }

    @EventHandler
    public void onOtherPlace(RobbingBlockPlaceEvent e) {
        if(e.getPlayer().getOpenInventory().getTopInventory().getHolder() instanceof SafeInventory)
            e.setCancelled(true);
    }
}
