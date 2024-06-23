package com.frahhs.robbing.gui;

import com.frahhs.robbing.RobbingListener;
import com.frahhs.robbing.gui.event.GUIClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GUIListener extends RobbingListener {

    @EventHandler
    public void onGuiClick(InventoryClickEvent e) {
        if(e.getClickedInventory() == null)
            return;

        if(! (e.getClickedInventory().getHolder() instanceof GUI))
            return;

        e.setCancelled(true);

        GUI gui = (GUI) e.getInventory().getHolder();
        assert gui != null;

        // Call StartStealEvent
        GUIClickEvent startRobbingEvent = new GUIClickEvent(gui, e);
        Bukkit.getPluginManager().callEvent(startRobbingEvent);
    }
}
