package com.frahhs.robbing.feature.rob.mcp;

import com.frahhs.lightlib.feature.LightModel;
import org.bukkit.entity.Player;

/**
 * LightController class for managing robbery actions.
 */
public class RobController extends LightModel {
    /**
     * Constructor for RobController.
     */
    public RobController() {}

    /**
     * Initiates the robbery action between a robber and a victim player.
     *
     * @param robber The player initiating the robbery.
     * @param victim The player being robbed.
     */
    public void startRobbing(Player robber, Player victim) {
        logger.fine("%s is robbing %s", robber.getName(), victim.getName());
        Rob.setRobbingNow(robber, victim);
        robber.openInventory(victim.getInventory());
    }

    /**
     * Stops the ongoing robbery action for a player.
     *
     * @param robber The player ending the robbery.
     */
    public void stopRobbing(Player robber) {
        logger.fine("%s robbing has been stopped", robber.getName());
        if (Rob.isRobbingNow(robber)) {
            Rob.removeRobbingNow(robber);
            robber.closeInventory();
        }
    }

    /**
     * Marks the player as successfully robbed, triggering cooldown.
     *
     * @param robber The player who successfully performed the robbery.
     */
    public void robbed(Player robber) {
        logger.fine("setting robbing cooldown on %s", robber.getName());
        Rob.setCooldown(robber);
    }
}
