package com.frahhs.robbing.feature.handcuffing.mcp;

import com.frahhs.lightlib.LightProvider;
import com.frahhs.robbing.feature.handcuffing.bag.HandcuffsBarBag;
import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * LightProvider class for managing handcuffs bar-related data and operations.
 */
class HandcuffsBarProvider extends LightProvider {
    private final HandcuffsBarBag handcuffsBarBag;

    /**
     * Constructs a HandcuffsBarProvider instance.
     */
    protected HandcuffsBarProvider() {
        handcuffsBarBag = (HandcuffsBarBag) bagManager.getBag("HandcuffsBarBag");
    }

    /**
     * Checks if a player has an active handcuffs health bar.
     *
     * @param player The player to check.
     * @return True if the player has an active health bar, otherwise false.
     */
    protected boolean haveHandcuffsBar(Player player) {
        return handcuffsBarBag.getData().containsKey(player);
    }

    /**
     * Retrieves the health bar associated with a player.
     *
     * @param player The player.
     * @return The health bar associated with the player, or null if not found.
     */
    protected HandcuffsBar getHandcuffsBar(Player player) {
        BossBar bar = handcuffsBarBag.getData().get(player);
        if (bar != null) {
            return new HandcuffsBar(player, bar);
        }
        return null;
    }

    /**
     * Adds the handcuffs bar to a player.
     *
     * @param player The player to add the health bar to.
     * @param bar    The health bar to add.
     */
    protected void saveHandcuffsBar(Player player, BossBar bar) {
        handcuffsBarBag.getData().put(player, bar);
    }

    /**
     * Removes the handcuffs bar from a player.
     *
     * @param player The player to remove the health bar from.
     */
    protected void removeHandcuffsBar(Player player) {
        handcuffsBarBag.getData().remove(player);
    }

    /**
     * Checks if a BossBar instance is associated with any handcuffs bar.
     *
     * @param bar The BossBar instance to check.
     * @return True if the BossBar is associated with a handcuffs bar, otherwise false.
     */
    protected boolean isHandcuffsBar(BossBar bar) {
        return handcuffsBarBag.getData().containsValue(bar);
    }

    /**
     * Retrieves a list of all active handcuffs bars.
     *
     * @return A list of HandcuffsBar instances representing all active handcuffs bars.
     */
    protected List<HandcuffsBar> getAllHandcuffsBar() {
        List<HandcuffsBar> bars = new ArrayList<>();
        for (Player player : handcuffsBarBag.getData().keySet()) {
            if (Bukkit.getOnlinePlayers().contains(player)) {
                bars.add(new HandcuffsBar(player, handcuffsBarBag.getData().get(player)));
            }
        }
        return bars;
    }
}
