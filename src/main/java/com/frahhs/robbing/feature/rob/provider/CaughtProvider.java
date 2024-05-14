package com.frahhs.robbing.feature.rob.provider;

import com.frahhs.robbing.feature.BaseProvider;
import com.frahhs.robbing.feature.rob.bag.CaughtBag;
import com.frahhs.robbing.feature.rob.bag.RobbingCooldownBag;
import com.frahhs.robbing.feature.rob.bag.RobbingNowBag;
import com.frahhs.robbing.util.Cooldown;
import org.bukkit.entity.Player;

public class CaughtProvider extends BaseProvider {
    private final CaughtBag caughtBag;

    public CaughtProvider() {
        caughtBag = (CaughtBag)bagManager.getBag("CaughtBag");
    }

    /**
     * Checks if a player is currently under a handcuffing cooldown.
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
     * @return The timestamp when the handcuffing action occurred.
     */
    public Cooldown getCooldown(Player caught) {
        return caughtBag.getData().get(caught);
    }

    /**
     * Sets the cooldown for the handcuffing action.
     */
    public void saveCooldown(Player caught, Cooldown cooldown) {
        caughtBag.getData().put(caught, cooldown);
    }

    /**
     * Remove the cooldown for the handcuffing action.
     */
    public void removeCooldown(Player caught) {
        caughtBag.getData().remove(caught);
    }
}
