package com.frahhs.robbing.features.handcuffing.controller;

import com.frahhs.robbing.features.handcuffing.models.KidnapPathModel;
import org.bukkit.entity.Player;

public class KidnapPathController {
    public void tick(Player kidnapper, Player kidnapped) {
        if(KidnapPathModel.getPathSize(kidnapper) > 10) {
            if(kidnapper.getLocation().distance(kidnapped.getLocation()) > 2)
                kidnapped.teleport(KidnapPathModel.getLocation(kidnapper, 10));
        }
    }
}
