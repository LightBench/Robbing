package com.frahhs.robbing.feature.rob.bag;

import com.frahhs.robbing.feature.Bag;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RobbingNowBag extends Bag {
    private Map<Player, Player> robbingNow;

    @Override
    protected void onEnable() {
        robbingNow = new ConcurrentHashMap<>();
    }

    @Override
    protected void onDisable() {
        robbingNow = null;
    }

    @Override
    protected String getID() {
        return "RobbingNowBag";
    }

    @Override
    public Map<Player, Player> getData() {
        return robbingNow;
    }
}
