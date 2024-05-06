package com.frahhs.robbing.features.handcuffing.controller;

import com.frahhs.robbing.features.handcuffing.models.KidnapPathModel;
import org.bukkit.entity.Player;

/**
 * Controller class for managing the path of a kidnapping.
 */
public class KidnapPathController {
    /**
     * Handles ticking of the kidnapper's path.
     *
     * @param kidnapper The player who is the kidnapper.
     * @param kidnapped The player who is kidnapped.
     */
    public void tick(Player kidnapper, Player kidnapped) {
        if(KidnapPathModel.getPathSize(kidnapper) > 10) {
            if(kidnapper.getLocation().distance(kidnapped.getLocation()) > 2)
                kidnapped.teleport(KidnapPathModel.getLocation(kidnapper, 10));
        }
    }
}
