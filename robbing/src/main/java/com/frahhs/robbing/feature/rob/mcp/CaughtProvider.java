package com.frahhs.robbing.feature.rob.mcp;

import com.frahhs.lightlib.LightProvider;
import com.frahhs.lightlib.util.Cooldown;
import com.frahhs.robbing.feature.rob.bag.CaughtBag;
import org.bukkit.entity.Player;

/**
 * LightProvider class for managing caught cooldowns.
 */
class CaughtProvider extends LightProvider {
    private final CaughtBag caughtBag;

    /**
     * Constructs a CaughtProvider object.
     */
    protected CaughtProvider() {
        caughtBag = (CaughtBag) bagManager.getBag("CaughtBag");
    }

    /**
     * Checks if a player is currently under a caught cooldown.
     *
     * @param caught The player to check.
     * @return True if the player is under cooldown, otherwise false.
     */
    protected boolean haveCooldown(Player caught) {
        return caughtBag.getData().containsKey(caught);
    }

    /**
     * Retrieves the cooldown timestamp for a player.
     *
     * @param caught The player to check.
     * @return The timestamp when the caught action occurred.
     */
    protected Cooldown getCooldown(Player caught) {
        return caughtBag.getData().get(caught);
    }

    /**
     * Sets the cooldown for the caught action.
     *
     * @param caught   The player to set the cooldown for.
     * @param cooldown The cooldown duration.
     */
    protected void saveCooldown(Player caught, Cooldown cooldown) {
        caughtBag.getData().put(caught, cooldown);
    }

    /**
     * Removes the cooldown for the caught action.
     *
     * @param caught The player to remove the cooldown for.
     */
    protected void removeCooldown(Player caught) {
        caughtBag.getData().remove(caught);
    }
}
