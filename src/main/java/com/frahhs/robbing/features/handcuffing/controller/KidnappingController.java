package com.frahhs.robbing.features.handcuffing.controller;

import com.frahhs.robbing.features.handcuffing.models.KidnappingModel;
import org.bukkit.entity.Player;

public class KidnappingController {
    public void kidnap(Player kidnapper, Player kidnapped) {
        KidnappingModel kidnappingModel = new KidnappingModel(kidnapper, kidnapped);
        kidnappingModel.kidnap();
    }

    public void free(Player kidnapped) {
        if(!KidnappingModel.isKidnapped(kidnapped))
            return;

        KidnappingModel.getFromKidnapped(kidnapped).free();
    }

    public Player getKidnapper(Player kidnapped) {
        if(!KidnappingModel.isKidnapped(kidnapped))
            return null;

        return KidnappingModel.getFromKidnapped(kidnapped).getKidnapper();
    }

    public Player getKidnapped(Player kidnapper) {
        if(!KidnappingModel.isKidnapper(kidnapper))
            return null;

        return KidnappingModel.getFromKidnapper(kidnapper).getKidnapped();
    }
}
