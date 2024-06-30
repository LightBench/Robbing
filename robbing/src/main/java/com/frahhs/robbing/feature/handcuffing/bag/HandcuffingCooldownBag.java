package com.frahhs.robbing.feature.handcuffing.bag;

import com.frahhs.lightlib.util.Cooldown;
import com.frahhs.lightlib.util.bag.Bag;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Bag to store handcuffing cooldowns for each player.
 * The key is the player who performed the handcuffing action,
 * and the value is the timestamp indicating when the action occurred.
 */
public class HandcuffingCooldownBag extends Bag {
    private Map<Player, Cooldown> handcuffingCooldown;

    @Override
    protected void onEnable() {
        handcuffingCooldown = new ConcurrentHashMap<>();
    }

    @Override
    protected void onDisable() {
        handcuffingCooldown = null;
    }

    @Override
    protected String getID() {
        return "HandcuffingCooldownBag";
    }

    @Override
    public Map<Player, Cooldown> getData() {
        return handcuffingCooldown;
    }
}
