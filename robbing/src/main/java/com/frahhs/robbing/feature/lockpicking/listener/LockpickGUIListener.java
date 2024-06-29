package com.frahhs.robbing.feature.lockpicking.listener;

import com.frahhs.lightlib.LightListener;
import com.frahhs.lightlib.LightPlugin;
import com.frahhs.lightlib.gui.event.GUIClickEvent;
import com.frahhs.lightlib.item.ItemManager;
import com.frahhs.robbing.feature.lockpicking.event.LockpickEvent;
import com.frahhs.robbing.feature.lockpicking.mcp.LockpickGUI;
import com.frahhs.robbing.feature.safe.mcp.SafeController;
import com.frahhs.robbing.feature.lockpicking.item.CylinderWrong;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class LockpickGUIListener extends LightListener {
    private final ItemManager itemManager;
    private final SafeController safeController;

    public LockpickGUIListener() {
        itemManager = LightPlugin.getItemsManager();
        safeController = new SafeController();
    }

    @EventHandler
    public void onSafeUnlockGUIClick(GUIClickEvent e) {
        if(!(e.getGui() instanceof LockpickGUI))
            return;

        LockpickGUI lockpickGUI = (LockpickGUI) e.getGui();
        Inventory inventory = lockpickGUI.getInventory();

        Player clicker = (Player) e.getInventoryClickEvent().getWhoClicked();
        int clickedSlot = e.getInventoryClickEvent().getSlot();

        if(clickedSlot == lockpickGUI.getCorrectCylinder()) {
            inventory.setItem(clickedSlot, itemManager.get("cylinder_correct").getItemStack());
            String message = messages.getMessage("lockpicking.success");
            e.getInventoryClickEvent().getWhoClicked().sendMessage(message);
            safeController.openInventory(((LockpickGUI) e.getGui()).getSafe(), clicker);
            LockpickEvent lockpickEvent = new LockpickEvent((Player) e.getInventoryClickEvent().getWhoClicked(), ((LockpickGUI) e.getGui()).getSafe(), true);
            Bukkit.getPluginManager().callEvent(lockpickEvent);
        } else if(lockpickGUI.isCylinderSlot(clickedSlot)){
            inventory.setItem(clickedSlot, itemManager.get("cylinder_wrong").getItemStack());
        }

        int failed_addemps = 0;
        for(int i = LockpickGUI.SLOT_CYLINDER_1; i <= LockpickGUI.SLOT_CYLINDER_9; i++) {
            ItemStack curCylinder = inventory.getItem(i);
            if(itemManager.get(curCylinder) instanceof CylinderWrong) {
                failed_addemps++;
            }
        }

        if(failed_addemps >= 2) {
            // Close inventory
            e.getInventoryClickEvent().getWhoClicked().closeInventory();

            // Break lockpick
            PlayerInventory playerInventory = e.getInventoryClickEvent().getWhoClicked().getInventory();
            playerInventory.getItemInMainHand().setAmount(playerInventory.getItemInMainHand().getAmount() - 1);

            // Send message
            String message = messages.getMessage("lockpicking.failed");
            e.getInventoryClickEvent().getWhoClicked().sendMessage(message);

            LockpickEvent lockpickEvent = new LockpickEvent((Player) e.getInventoryClickEvent().getWhoClicked(), ((LockpickGUI) e.getGui()).getSafe(), false);
            Bukkit.getPluginManager().callEvent(lockpickEvent);
        }
    }
}
