package com.frahhs.robbing.feature.safe.bag;

import com.frahhs.lightlib.util.bag.Bag;
import com.frahhs.robbing.feature.safe.mcp.SafeInventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SafeInventoryBag extends Bag {
    private Map<UUID, SafeInventory> safeInventory;

    @Override
    protected void onEnable() {
        safeInventory = new ConcurrentHashMap<>();
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
