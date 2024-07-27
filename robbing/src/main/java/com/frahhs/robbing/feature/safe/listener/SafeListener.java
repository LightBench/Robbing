package com.frahhs.robbing.feature.safe.listener;

import com.frahhs.lightlib.LightListener;
import com.frahhs.lightlib.LightPlugin;
import com.frahhs.lightlib.block.events.LightBlockBreakEvent;
import com.frahhs.lightlib.block.events.LightBlockInteractEvent;
import com.frahhs.lightlib.block.events.LightBlockPlaceEvent;
import com.frahhs.lightlib.item.ItemManager;
import com.frahhs.robbing.feature.safe.mcp.SafeController;
import com.frahhs.robbing.feature.safe.mcp.SafeInventory;
import com.frahhs.robbing.feature.safe.mcp.SafeModel;
import com.frahhs.robbing.feature.lockpicking.item.Lockpick;
import com.frahhs.robbing.feature.safe.item.Safe;
import com.frahhs.robbing.feature.safe.mcp.SafeUnlockGUI;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SafeListener extends LightListener {
    private final SafeController safeController;
    private final ItemManager itemManager;

    public SafeListener() {
        safeController = new SafeController();
        itemManager = LightPlugin.getItemsManager();
    }

    @EventHandler
    public void onOpen(LightBlockInteractEvent e) {
        if(!(e.getBlock().getItem() instanceof Safe))
            return;

        if(e.getPlayer().isSneaking() && !( e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.AIR)))
            return;

        assert e.getHand() != null;
        if(!e.getHand().equals(EquipmentSlot.OFF_HAND))
            return;

        if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            return;

        if(!e.getPlayer().hasPermission("robbing.use_safe")) {
            String message = messages.getMessage("general.no_permissions");
            e.getPlayer().sendMessage(message);
            return;
        }

        ItemStack itemInMainHand = e.getPlayer().getInventory().getItemInMainHand();
        if(SafeModel.isLocked(e.getBlock()) && itemManager.get(itemInMainHand) instanceof Lockpick)
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
    public void onPlace(LightBlockPlaceEvent e) {
        if(!(e.getBlock().getItem() instanceof Safe))
            return;

        if(!e.isCancelled())
            safeController.placeBlock(e.getBlock(), e.getItemInHand());
    }

    @EventHandler
    public void onBreak(LightBlockBreakEvent e) {
        if(!(e.getBlock().getItem() instanceof Safe))
            return;

        if(config.getBoolean("safe.only_owner_can_break")) {
            if(!e.getPlayer().hasPermission("robbing.admin")) {
                Player locker = SafeModel.getFromSafe(e.getBlock()).getLocker();
                if(locker != null) {
                    if(!locker.getUniqueId().equals(e.getPlayer().getUniqueId())) {
                        String message = messages.getMessage("safes.cant_break_claimed_safe");
                        e.getPlayer().sendMessage(message);
                        e.setCancelled(true);
                        return;
                    }
                }
            }
        }

        // Drop option
        if(!e.isCancelled() && e.isDropItems() && !e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            e.setDropItems(false);

            // Close safe gui on safe break
            for(Player player : Bukkit.getOnlinePlayers()) {
                if(player.getOpenInventory().getTopInventory().getHolder() instanceof SafeUnlockGUI) {
                    player.closeInventory();
                }
            }

            // Close safe inventory on safe break
            List<HumanEntity> viewers = SafeModel.getFromSafe(e.getBlock()).getInventory().getViewers();
            for(int i = 0; i < viewers.size(); i ++) {
                viewers.get(i).closeInventory();
            }

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
    public void onOtherPlace(LightBlockPlaceEvent e) {
        if(e.getPlayer().getOpenInventory().getTopInventory().getHolder() instanceof SafeInventory)
            e.setCancelled(true);
    }
}
