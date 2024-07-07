package com.frahhs.robbing.feature.safe.mcp;

import com.frahhs.lightlib.block.LightBlock;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class SafeInventory implements InventoryHolder {
    LightBlock safe;
    private final Inventory inventory;

    public SafeInventory(LightBlock safe) {
        this.safe = safe;
        this.inventory = Bukkit.createInventory(this, 9 * 6, "Safe");
    }

    public LightBlock getSafe() {
        return safe;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
