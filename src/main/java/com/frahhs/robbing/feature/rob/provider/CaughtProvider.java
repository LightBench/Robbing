package com.frahhs.robbing.feature.rob.provider;

import com.frahhs.robbing.feature.Provider;
import com.frahhs.robbing.feature.rob.bag.CaughtBag;
import com.frahhs.robbing.util.Cooldown;
import org.bukkit.entity.Player;

/**
 * Provider class for managing caught cooldowns.
 */
public class CaughtProvider extends Provider {
    private final CaughtBag caughtBag;

    /**
     * Constructs a CaughtProvider object.
     */
    public CaughtProvider() {
        caughtBag = (CaughtBag) bagManager.getBag("CaughtBag");
    }

    /**
     * Checks if a player is currently under a caught cooldown.
     *
     * @param caught The player to check.
     * @return True if the player is under cooldown, otherwise false.
     */
    public boolean haveCooldown(Player caught) {
        return caughtBag.getData().containsKey(caught);
    }

    /**
     * Retrieves the cooldown timestamp for a player.
     *
     * @param caught The player to check.
     * @return The timestamp when the caught action occurred.
     */
    public Cooldown getCooldown(Player caught) {
        return caughtBag.getData().get(caught);
    }

    /**
     * Sets the cooldown for the caught action.
     *
     * @param caught   The player to set the cooldown for.
     * @param cooldown The cooldown duration.
     */
    public void saveCooldown(Player caught, Cooldown cooldown) {
        caughtBag.getData().put(caught, cooldown);
    }

    /**
     * Removes the cooldown for the caught action.
     *
     * @param caught The player to remove the cooldown for.
     */
    public void removeCooldown(Player caught) {
        caughtBag.getData().remove(caught);
    }
}
