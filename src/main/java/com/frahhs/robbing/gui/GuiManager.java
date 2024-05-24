package com.frahhs.robbing.gui;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class GuiManager {
    public Inventory get(Gui gui) {
        switch(gui){
            case SAFE:
                return Bukkit.createInventory(null, 6*9, "\uF001§f\uD83D\uDE97");
        }

        return null;
    }


}
