package com.frahhs.robbing.feature.kidnapping.bag;

import com.frahhs.robbing.util.bag.Bag;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class KidnappingBag extends Bag {
    private Map<Player, Player> kidnapping;

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
    public Map<Player, Player> getData() {
        return kidnapping;
    }
}
