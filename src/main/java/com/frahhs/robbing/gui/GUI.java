package com.frahhs.robbing.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public interface GUI extends InventoryHolder {
    @NotNull
    @Override
    public Inventory getInventory();

    public GUIType getType();
}
