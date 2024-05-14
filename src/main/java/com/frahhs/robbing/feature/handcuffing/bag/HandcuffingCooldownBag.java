package com.frahhs.robbing.feature.handcuffing.bag;

import com.frahhs.robbing.bag.Bag;
import com.frahhs.robbing.util.Cooldown;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Bag to store handcuffing cooldowns for each player.
 * The key is the player who performed the handcuffing action,
 * and the value is the timestamp indicating when the action occurred.
 */
public class HandcuffingCooldownBag extends Bag {
    public Map<Player, Cooldown> handcuffingCooldown;

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
    public Map<Player, Cooldown> getMap() {
        return handcuffingCooldown;
    }

    /**
     * Sets the cooldown for the handcuffing action.
     */
    /*public void setCooldown(Player handcuffer, int time) {
        new Thread(() -> {
            try {
                Cooldown cooldown = new Cooldown(System.currentTimeMillis(), time);
                handcuffingCooldown.put(handcuffer, cooldown);
                Thread.sleep(time * 1000L);
                handcuffingCooldown.remove(handcuffer.getPlayer());
            } catch(InterruptedException v) {
                System.out.println(v.getMessage());
            }
        }).start();
    }*/
}
