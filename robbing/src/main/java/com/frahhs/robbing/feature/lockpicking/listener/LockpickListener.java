package com.frahhs.robbing.feature.lockpicking.listener;

import com.frahhs.lightlib.LightListener;
import com.frahhs.lightlib.LightPlugin;
import com.frahhs.lightlib.block.events.LightBlockInteractEvent;
import com.frahhs.lightlib.item.ItemManager;
import com.frahhs.robbing.feature.lockpicking.mcp.LockpickController;
import com.frahhs.robbing.feature.safe.mcp.SafeController;
import com.frahhs.robbing.feature.safe.mcp.SafeModel;
import com.frahhs.robbing.feature.lockpicking.item.Lockpick;
import com.frahhs.robbing.feature.safe.item.Safe;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class LockpickListener extends LightListener {
    private final ItemManager itemManager;
    private final LockpickController lockpickController;
    private final SafeController safeController;

    public LockpickListener() {
        itemManager = LightPlugin.getItemsManager();
        lockpickController = new LockpickController();
        safeController = new SafeController();
    }

    @EventHandler
    public void lockpick(LightBlockInteractEvent e) {
        if(!(e.getBlock().getItem() instanceof Safe))
            return;

        assert e.getHand() != null;
        if(!e.getHand().equals(EquipmentSlot.HAND))
            return;

        ItemStack itemInMainHand = e.getPlayer().getInventory().getItemInMainHand();
        if(!(LightPlugin.getItemsManager().get(itemInMainHand) instanceof Lockpick))
            return;

        if(!SafeModel.isLocked(e.getBlock()))
            return;

        if(!e.getPlayer().hasPermission("robbing.lockpick")) {
            String message = messages.getMessage("general.no_permissions_item");
            e.getPlayer().sendMessage(message);
            return;
        }

        lockpickController.openGUI(e.getPlayer(), e.getBlock());
    }
}
