package com.frahhs.robbing.feature.rob.provider;

import com.frahhs.robbing.feature.Provider;
import com.frahhs.robbing.feature.rob.bag.RobbingCooldownBag;
import com.frahhs.robbing.feature.rob.bag.RobbingNowBag;
import com.frahhs.robbing.util.Cooldown;
import org.bukkit.entity.Player;

/**
 * Provider class for managing robbery actions.
 */
public class RobProvider extends Provider {
    private final RobbingNowBag robbingNowBag;
    private final RobbingCooldownBag robbingCooldownBag;

    /**
     * Constructs a RobProvider object.
     */
    public RobProvider() {
        robbingNowBag = (RobbingNowBag) bagManager.getBag("RobbingNowBag");
        robbingCooldownBag = (RobbingCooldownBag) bagManager.getBag("RobbingCooldownBag");
    }

    /**
     * Checks if a player is currently initiating a robbery.
     *
     * @param robber The player to check.
     * @return True if the player is initiating a robbery, otherwise false.
     */
    public boolean isRobbingNow(Player robber) {
        return robbingNowBag.getData().containsKey(robber);
    }

    /**
     * Checks if a player is currently being robbed.
     *
     * @param robbed The player to check.
     * @return True if the player is being robbed, otherwise false.
     */
    public boolean isRobbedNow(Player robbed) {
        return robbingNowBag.getData().containsValue(robbed);
    }

    /**
     * Saves the ongoing robbery action.
     *
     * @param robber The player initiating the robbery.
     * @param robbed The player being robbed.
     */
    public void saveRobbingNow(Player robber, Player robbed) {
        robbingNowBag.getData().put(robber, robbed);
    }

    /**
     * Removes the ongoing robbery action for a player.
     *
     * @param robber The player ending the robbery.
     */
    public void removeRobbingNow(Player robber) {
        robbingNowBag.getData().remove(robber);
    }

    /**
     * Checks if a player is currently under a robbery cooldown.
     *
     * @param robber The player to check.
     * @return True if the player is under cooldown, otherwise false.
     */
    public boolean haveCooldown(Player robber) {
        return robbingCooldownBag.getData().containsKey(robber);
    }

    /**
     * Retrieves the cooldown timestamp for a player.
     *
     * @param robber The player to check.
     * @return The timestamp when the robbery action occurred.
     */
    public Cooldown getCooldown(Player robber) {
        return robbingCooldownBag.getData().get(robber);
    }

    /**
     * Sets the cooldown for the robbery action.
     *
     * @param robber   The player to set the cooldown for.
     * @param cooldown The cooldown duration.
     */
    public void saveCooldown(Player robber, Cooldown cooldown) {
        robbingCooldownBag.getData().put(robber, cooldown);
    }

    /**
     * Removes the cooldown for the robbery action.
     *
     * @param robber The player to remove the cooldown for.
     */
    public void removeCooldown(Player robber) {
        robbingCooldownBag.getData().remove(robber);
    }
}
