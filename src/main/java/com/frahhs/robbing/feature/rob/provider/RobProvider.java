package com.frahhs.robbing.feature.rob.provider;

import com.frahhs.robbing.feature.Provider;
import com.frahhs.robbing.feature.rob.bag.RobbingCooldownBag;
import com.frahhs.robbing.feature.rob.bag.RobbingNowBag;
import com.frahhs.robbing.util.Cooldown;
import org.bukkit.entity.Player;

public class RobProvider extends Provider {
    private final RobbingNowBag robbingNowBag;
    private final RobbingCooldownBag robbingCooldownBag;

    public RobProvider() {
        robbingNowBag = (RobbingNowBag)bagManager.getBag("RobbingNowBag");
        robbingCooldownBag = (RobbingCooldownBag)bagManager.getBag("RobbingCooldownBag");
    }

    public boolean isRobbingNow(Player robber) {
        return robbingNowBag.getData().containsKey(robber);
    }

    public boolean isRobbedNow(Player robbed) {
        return robbingNowBag.getData().containsValue(robbed);
    }

    public void saveRobbingNow(Player robber, Player robbed) {
        robbingNowBag.getData().put(robber, robbed);
    }

    public void removeRobbingNow(Player robber) {
        robbingNowBag.getData().remove(robber);
    }

    /**
     * Checks if a player is currently under a robbing cooldown.
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
     * @return The timestamp when the robbing action occurred.
     */
    public Cooldown getCooldown(Player robber) {
        return robbingCooldownBag.getData().get(robber);
    }

    /**
     * Sets the cooldown for the robbing action.
     */
    public void saveCooldown(Player robber, Cooldown cooldown) {
        robbingCooldownBag.getData().put(robber, cooldown);
    }

    /**
     * Remove the cooldown for the robbing action.
     */
    public void removeCooldown(Player robber) {
        robbingCooldownBag.getData().remove(robber);
    }
}
