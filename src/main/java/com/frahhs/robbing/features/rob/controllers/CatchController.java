package com.frahhs.robbing.features.rob.controllers;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller class for managing caught players.
 */
public class CatchController {

    public static List<Player> caughtList = new ArrayList<>();

    /**
     * Adds a player to the list of caught players.
     *
     * @param player The player to add.
     */
    public void addCaught(Player player) {
        caughtList.add(player);
    }

    /**
     * Removes a player from the list of caught players.
     *
     * @param player The player to remove.
     */
    public void removeCaught(Player player) {
        caughtList.remove(player);
    }

    /**
     * Checks if a player is in the list of caught players.
     *
     * @param player The player to check.
     * @return True if the player is caught, false otherwise.
     */
    public boolean isCaught(Player player) {
        return caughtList.contains(player);
    }
}
