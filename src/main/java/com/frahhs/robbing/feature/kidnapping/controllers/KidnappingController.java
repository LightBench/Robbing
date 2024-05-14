package com.frahhs.robbing.feature.kidnapping.controllers;

import com.frahhs.robbing.feature.BaseController;
import com.frahhs.robbing.feature.kidnapping.models.KidnappingModel;
import org.bukkit.entity.Player;

/**
 * Controller class for managing kidnapping operations.
 */
public class KidnappingController extends BaseController {
    /**
     * Initiates a kidnapping.
     *
     * @param kidnapper The player who is the kidnapper.
     * @param kidnapped The player who is kidnapped.
     */
    public void kidnap(Player kidnapper, Player kidnapped) {
        KidnappingModel kidnappingModel = new KidnappingModel(kidnapper, kidnapped);
        kidnappingModel.kidnap();
    }

    /**
     * Frees a kidnapped player.
     *
     * @param kidnapped The player who is kidnapped.
     */
    public void free(Player kidnapped) {
        if(!KidnappingModel.isKidnapped(kidnapped))
            return;

        KidnappingModel.getFromKidnapped(kidnapped).free();
    }

    /**
     * Retrieves the kidnapper of a kidnapped player.
     *
     * @param kidnapped The player who is kidnapped.
     * @return The player who is the kidnapper, or null if not found.
     */
    public Player getKidnapper(Player kidnapped) {
        if(!KidnappingModel.isKidnapped(kidnapped))
            return null;

        return KidnappingModel.getFromKidnapped(kidnapped).getKidnapper();
    }

    /**
     * Retrieves the kidnapped player of a kidnapper.
     *
     * @param kidnapper The player who is the kidnapper.
     * @return The player who is kidnapped, or null if not found.
     */
    public Player getKidnapped(Player kidnapper) {
        if(!KidnappingModel.isKidnapper(kidnapper))
            return null;

        return KidnappingModel.getFromKidnapper(kidnapper).getKidnapped();
    }
}
