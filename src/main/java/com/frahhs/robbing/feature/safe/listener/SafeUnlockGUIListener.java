package com.frahhs.robbing.feature.safe.listener;

import com.frahhs.robbing.RobbingListener;
import com.frahhs.robbing.feature.safe.mcp.SafeController;
import com.frahhs.robbing.feature.safe.mcp.SafeUnlockGUI;
import com.frahhs.robbing.gui.GUIType;
import com.frahhs.robbing.gui.event.GUIClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import static com.frahhs.robbing.feature.safe.mcp.SafeUnlockGUI.*;

public class SafeUnlockGUIListener extends RobbingListener {
    @EventHandler
    public void onSafeUnlockGUIClick(GUIClickEvent e) {
        if(!e.getGui().getType().equals(GUIType.SAFE_UNLOCK))
            return;

        SafeUnlockGUI safeUnlockGUI = (SafeUnlockGUI) e.getGui();

        Player clicker = (Player) e.getInventoryClickEvent().getWhoClicked();

        switch(e.getInventoryClickEvent().getSlot()) {
            case SLOT_PANEL_0:
            case SLOT_PANEL_1:
            case SLOT_PANEL_2:
            case SLOT_PANEL_3:
            case SLOT_PANEL_4:
            case SLOT_PANEL_5:
            case SLOT_PANEL_6:
            case SLOT_PANEL_7:
            case SLOT_PANEL_8:
            case SLOT_PANEL_9:
                safeUnlockGUI.insertPinDigit(e.getInventoryClickEvent().getSlot());
                break;
            case SLOT_PANEL_CHECK:
                if(safeUnlockGUI.checkPin()) {
                    SafeController safeController = new SafeController();
                    safeController.openInventory(safeUnlockGUI.getSafe(), clicker);
                } else {
                    clicker.closeInventory();
                    clicker.sendMessage("Wrong pin"); // TODO: add to messages
                }
                break;
            case SLOT_PANEL_CANCEL:
                safeUnlockGUI.cancelPin();
                break;
        }
    }
}
