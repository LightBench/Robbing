package com.frahhs.robbing.feature.rob.bag;

import com.frahhs.lightlib.util.Cooldown;
import com.frahhs.lightlib.util.bag.Bag;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Bag to store robbing cooldowns for each player.
 * The key is the player who performed the robbing action,
 * and the cooldown indicate the time to wait until to perform the action again.
 */
public class RobbingCooldownBag extends Bag {
    private Map<Player, Cooldown> robCooldown;

    @Override
    protected void onEnable() {
        robCooldown = new ConcurrentHashMap<>();
    }

    @Override
    protected void onDisable() {
        robCooldown = null;
    }

    @Override
    protected String getID() {
        return "RobbingCooldownBag";
    }

    @Override
    public Map<Player, Cooldown> getData() {
        return robCooldown;
    }
}
