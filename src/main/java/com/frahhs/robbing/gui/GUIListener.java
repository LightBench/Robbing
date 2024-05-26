package com.frahhs.robbing.gui;

import com.frahhs.robbing.RobbingListener;
import com.frahhs.robbing.feature.safe.SafeUnlockGUI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GUIListener extends RobbingListener {

    @EventHandler
    public void onGuiClick(InventoryClickEvent e) {
        if(! (e.getInventory().getHolder() instanceof SafeUnlockGUI))
            return;

        e.setCancelled(true);

        SafeUnlockGUI gui = (SafeUnlockGUI) e.getInventory().getHolder();
        gui.onInventoryClick(e);
    }
}
