package com.frahhs.robbing.features.handcuffing.controllers;

import com.frahhs.robbing.features.BaseController;
import com.frahhs.robbing.features.handcuffing.models.HandcuffsBarModel;
import com.frahhs.robbing.features.kidnapping.controllers.KidnappingController;
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
        if (HandcuffsBarModel.haveBar(player))
            return;

        HandcuffsBarModel handcuffsBar = new HandcuffsBarModel(player);
        handcuffsBar.putHandcuffsBar();
    }

    /**
     * Removes the handcuffs bar from a player.
     *
     * @param player The player to remove the handcuffs bar from.
     */
    public void remove(Player player) {
        if (!HandcuffsBarModel.haveBar(player))
            return;

        HandcuffsBarModel handcuffsBar = HandcuffsBarModel.getBarFromPlayer(player);
        handcuffsBar.removeHandcuffsBar();
    }

    /**
     * Iterates the cracking of the handcuffs bar.
     *
     * @param player The player whose handcuffs bar is being iterated.
     * @return True if the health bar is broken, false otherwise.
     */
    public boolean hit(Player player) {
        if (!HandcuffsBarModel.haveBar(player)) {
            logger.unexpectedError("N16TOV");
            throw new RuntimeException();
        }

        HandcuffsBarModel handcuffsBar = HandcuffsBarModel.getBarFromPlayer(player);
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

    /**
     * Called when the plugin is enabled.
     */
    public void onEnable() {
        HandcuffsBarModel.putAllHandcuffsLifeModel();
    }

    /**
     * Called when the plugin is disabled.
     */
    public void onDisable() {
        HandcuffsBarModel.removeAllHandcuffsLifeModel();
    }
}
