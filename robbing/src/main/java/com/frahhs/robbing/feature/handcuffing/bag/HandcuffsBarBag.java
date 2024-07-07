package com.frahhs.robbing.feature.handcuffing.bag;

import com.frahhs.lightlib.util.bag.Bag;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Bag to store active handcuffs bars for each player.
 */
public class HandcuffsBarBag extends Bag {
    private Map<Player, BossBar> bars;

    @Override
    protected void onEnable() {
        bars = new ConcurrentHashMap<>();
    }

    @Override
    protected void onDisable() {
        bars = null;
    }

    @Override
    protected String getID() {
        return "HandcuffsBarBag";
    }

    @Override
    public Map<Player, BossBar> getData() {
        return bars;
    }
}
