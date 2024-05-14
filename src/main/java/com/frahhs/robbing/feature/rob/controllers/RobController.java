package com.frahhs.robbing.feature.rob.controllers;

import com.frahhs.robbing.feature.BaseModel;
import com.frahhs.robbing.feature.rob.model.Rob;
import org.bukkit.entity.Player;

/**
 * Controller class for managing robbery actions.
 */
public class RobController extends BaseModel {
    /**
     * Constructor for RobController.
     */
    public RobController() {}

    /**
     * Starts the robbery action between a robber and a robbed player.
     *
     * @param robber The player initiating the robbery.
     * @param robbed The player being robbed.
     */
    public void startRobbing(Player robber, Player robbed) {
        Rob.setRobbingNow(robber, robbed);
        robber.openInventory(robbed.getInventory());
    }

    /**
     * Stops the ongoing robbery action for a player.
     *
     * @param robber The player ending the robbery.
     */
    public void stopRobbing(Player robber) {
        if (Rob.isRobbingNow(robber)) {
            Rob.removeRobbingNow(robber);
            robber.closeInventory();
        }
    }

    // Called if the player really performed the action
    public void robbed(Player robber) {
        Rob.setCooldown(robber);
    }
}
