package com.frahhs.robbing.feature.handcuffing.mcp;

import com.frahhs.robbing.feature.Controller;
import com.frahhs.robbing.feature.kidnapping.mcp.KidnappingController;
import org.bukkit.entity.Player;

/**
 * Controller class for managing handcuffs bar actions.
 */
public class HandcuffsBarController extends Controller {
    /**
     * Puts the handcuffs bar on a player, if he doesn't have it.
     *
     * @param player The player to put the handcuffs bar on.
     */
    public void put(Player player) {
        logger.fine("%s handcuffs put", player.getName());
        if (HandcuffsBar.haveBar(player))
            return;

        HandcuffsBar handcuffsBar = new HandcuffsBar(player);
        handcuffsBar.putHandcuffsBar();
    }

    /**
     * Removes the handcuffs bar from a player, if he has it.
     *
     * @param player The player to remove the handcuffs bar from.
     */
    public void remove(Player player) {
        logger.fine("%s handcuffs removed", player.getName());
        if (!HandcuffsBar.haveBar(player))
            return;

        HandcuffsBar handcuffsBar = HandcuffsBar.getBarFromPlayer(player);
        assert handcuffsBar != null;
        handcuffsBar.removeHandcuffsBar();
    }

    /**
     * Iterates one time the cracking of the handcuffs bar.
     *
     * @param player The player whose handcuffs bar is being iterated.
     * @return True if the health bar has got broken, false otherwise.
     * @throws RuntimeException if the player does not have a handcuffs bar.
     */
    public boolean hit(Player player) {
        logger.fine("%s is hitting handcuffs", player.getName());
        if (!HandcuffsBar.haveBar(player)) {
            throw new RuntimeException(String.format("Handcuffs bar not found for player %s.", player.getName()));
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

            logger.fine("%s has broken the handcuffs", player.getName());
            return true;
        }
        logger.fine("%s hit handcuffs", player.getName());
        return false;
    }
}
