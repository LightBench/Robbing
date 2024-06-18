package com.frahhs.robbing.feature.lockpicking.listener;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.RobbingListener;
import com.frahhs.robbing.block.events.RobbingBlockInteractEvent;
import com.frahhs.robbing.feature.lockpicking.mcp.LockpickController;
import com.frahhs.robbing.feature.lockpicking.mcp.LockpickGUI;
import com.frahhs.robbing.feature.safe.mcp.SafeController;
import com.frahhs.robbing.feature.safe.mcp.SafeModel;
import com.frahhs.robbing.item.ItemManager;
import com.frahhs.robbing.item.RobbingMaterial;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class LockpickListener extends RobbingListener {
    private final ItemManager itemManager;
    private final LockpickController lockpickController;
    private final SafeController safeController;

    public LockpickListener() {
        itemManager = Robbing.getInstance().getItemsManager();
        lockpickController = new LockpickController();
        safeController = new SafeController();
    }

    @EventHandler
    public void lockpick(RobbingBlockInteractEvent e) {
        if(! (e.getBlock().getRobbingMaterial() == RobbingMaterial.SAFE))
            return;

        if(!e.getHand().equals(EquipmentSlot.HAND))
            return;

        ItemStack itemInMainHand = e.getPlayer().getInventory().getItemInMainHand();
        if(!itemInMainHand.isSimilar(itemManager.get(RobbingMaterial.LOCKPICK).getItemStack()))
            return;

        if(!SafeModel.isLocked(e.getBlock()))
            return;

        lockpickController.openGUI(e.getPlayer(), e.getBlock());
    }
}
