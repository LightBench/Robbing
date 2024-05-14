package com.frahhs.robbing.feature.handcuffing.controller;

import com.frahhs.robbing.feature.BaseController;
import com.frahhs.robbing.feature.handcuffing.model.HandcuffsBar;
import com.frahhs.robbing.feature.kidnapping.controllers.KidnappingController;
import org.bukkit.entity.Player;

/**
 * Controller class for managing handcuffs bar actions.
 */
public class HandcuffsBarController extends BaseController {
    /**
     * Puts the handcuffs bar on a player.
     *
     * @param player The player to put the handcuffs bar on.
     */
    public void put(Player player) {
        if (HandcuffsBar.haveBar(player))
            return;

        HandcuffsBar handcuffsBar = new HandcuffsBar(player);
        handcuffsBar.putHandcuffsBar();
    }

    /**
     * Removes the handcuffs bar from a player.
     *
     * @param player The player to remove the handcuffs bar from.
     */
    public void remove(Player player) {
        if (!HandcuffsBar.haveBar(player))
            return;

        HandcuffsBar handcuffsBar = HandcuffsBar.getBarFromPlayer(player);
        handcuffsBar.removeHandcuffsBar();
    }

    /**
     * Iterates the cracking of the handcuffs bar.
     *
     * @param player The player whose handcuffs bar is being iterated.
     * @return True if the health bar is broken, false otherwise.
     */
    public boolean hit(Player player) {
        if (!HandcuffsBar.haveBar(player)) {
            logger.unexpectedError("N16TOV");
            throw new RuntimeException();
        }

        HandcuffsBar handcuffsBar = HandcuffsBar.getBarFromPlayer(player);
        HandcuffingController handcuffingController = new HandcuffingController();
        KidnappingController kidnappingController = new KidnappingController();

        if (handcuffsBar.hit()) {
            // Remove handcuffs
            handcuffingController.removeHandcuffs(player, true);

            // Free handcuffed if was kidnapped
            kidnappingController.free(player);

            // Remove bar
            remove(player);

            return true;
        }
        return false;
    }
}
