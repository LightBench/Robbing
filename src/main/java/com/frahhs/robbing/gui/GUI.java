package com.frahhs.robbing.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public interface GUI extends InventoryHolder {
    @NotNull
    @Override
    public Inventory getInventory();

    public void onInventoryClick(InventoryClickEvent e);

    public GUIType getType();
}
