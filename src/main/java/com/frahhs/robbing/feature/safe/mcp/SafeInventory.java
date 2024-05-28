package com.frahhs.robbing.feature.safe.mcp;

import com.frahhs.robbing.block.RobbingBlock;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class SafeInventory implements InventoryHolder {
    RobbingBlock safe;
    private final Inventory inventory;

    public SafeInventory(RobbingBlock safe) {
        this.safe = safe;
        this.inventory = Bukkit.createInventory(this, 9 * 6, "Safe");
    }

    public RobbingBlock getSafe() {
        return safe;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
