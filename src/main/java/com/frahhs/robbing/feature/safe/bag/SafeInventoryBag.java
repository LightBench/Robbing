package com.frahhs.robbing.feature.safe.bag;

import com.frahhs.robbing.feature.safe.mcp.SafeInventory;
import com.frahhs.robbing.util.bag.Bag;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SafeInventoryBag extends Bag {
    private Map<UUID, SafeInventory> safeInventory;

    @Override
    protected void onEnable() {
        safeInventory = new HashMap<>();
    }

    @Override
    protected void onDisable() {
        safeInventory = null;
    }

    @Override
    protected String getID() {
        return "SafeInventoryBag";
    }

    @Override
    public Map<UUID, SafeInventory> getData() {
        return safeInventory;
    }
}
