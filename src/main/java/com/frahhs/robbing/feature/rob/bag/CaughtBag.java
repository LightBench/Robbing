package com.frahhs.robbing.feature.rob.bag;

import com.frahhs.robbing.util.bag.Bag;
import com.frahhs.robbing.util.Cooldown;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CaughtBag extends Bag {
    private Map<Player, Cooldown> caught;

    @Override
    protected void onEnable() {
        caught = new ConcurrentHashMap<>();
    }

    @Override
    protected void onDisable() {
        caught = null;
    }

    @Override
    protected String getID() {
        return "CaughtBag";
    }

    @Override
    public Map<Player, Cooldown> getData() {
        return caught;
    }
}
