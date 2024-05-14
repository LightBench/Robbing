package com.frahhs.robbing.feature.handcuffing.bag;

import com.frahhs.robbing.bag.Bag;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Model class representing the health bar for handcuffs.
 */
public class HandcuffsBarBag extends Bag {
    /** Map to store active handcuffs health bars for each player. */
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
    public Map<Player, BossBar> getMap() {
        return bars;
    }

    /**
     * Checks if a player has an active handcuffs health bar.
     *
     * @return True if the player has an active health bar, otherwise false.
     */
    /*public boolean haveHandcuffsBar(Player player) {
        return bars.containsKey(player);
    }*/

    /**
     * Retrieves the health bar associated with a player.
     *
     * @param player The player.
     * @return The health bar associated with the player.
     */
    /*public BossBar getHandcuffsBar(Player player) {
        return bars.get(player);
    }*/

    /**
     * Adds the handcuffs bar to a player.
     */
    /*public void putHandcuffsBar(Player player, BossBar bar) {
        bars.put(player, bar);
    }*/

    /**
     * Removes the handcuffs bar from a player.
     */
    /*public void removeHandcuffsBar(Player player) {
        bars.remove(player);
    }*/
}
