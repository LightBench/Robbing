package com.frahhs.robbing.feature.kidnapping.bag;

import com.frahhs.lightlib.util.bag.Bag;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KidnappingBag extends Bag {
    private Map<UUID, UUID> kidnapping;

    @Override
    protected void onEnable() {
        kidnapping = new HashMap<>();
    }

    @Override
    protected void onDisable() {
        kidnapping = null;
    }

    @Override
    protected String getID() {
        return "KidnappingBag";
    }

    @Override
    public Map<UUID, UUID> getData() {
        return kidnapping;
    }
}
