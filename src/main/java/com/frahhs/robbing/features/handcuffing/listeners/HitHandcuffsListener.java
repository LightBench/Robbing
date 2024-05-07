package com.frahhs.robbing.features.handcuffing.listeners;

import com.frahhs.robbing.features.BaseListener;
import com.frahhs.robbing.features.handcuffing.controllers.HandcuffingController;
import com.frahhs.robbing.features.handcuffing.controllers.HandcuffsBarController;
import com.frahhs.robbing.features.handcuffing.events.ToggleHandcuffsEvent;
import com.frahhs.robbing.features.handcuffing.models.HandcuffingModel;
import com.frahhs.robbing.features.kidnapping.controllers.KidnappingController;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class HitHandcuffsListener extends BaseListener {
    HandcuffingController handcuffingController;
    KidnappingController kidnappingController;
    HandcuffsBarController handcuffsBarController;

    public HitHandcuffsListener() {
        handcuffingController = new HandcuffingController();
        kidnappingController = new KidnappingController();
        handcuffsBarController = new HandcuffsBarController();

        handcuffsBarController.onEnable();
    }

    @EventHandler
    public void PlaceHandcuffsLifeModel(ToggleHandcuffsEvent e) {
        if(e.isCancelled())
            return;

        // If it is putting on handcuffs add HealthBar, else remove it
        if(e.isPuttingOn()) {
            handcuffsBarController.put(e.getHandcuffed());
        } else {
            handcuffsBarController.remove(e.getHandcuffed());
        }
    }

    @EventHandler
    public void crackHealthBar(PlayerToggleSneakEvent e) {
        // Check if player is handcuffed
        if(!HandcuffingModel.isHandcuffed(e.getPlayer()))
            return;

        // Check if player is not sneaking
        if(!e.isSneaking())
            return;

        Player handcuffed = e.getPlayer();

        // Iterate crack to handcuffed bar
        if(handcuffsBarController.hit(handcuffed)) {
            // Send message
            String message = messages.getMessage("handcuffing.broken_handcuffs");
            handcuffed.sendMessage(message);
        }
    }

    @EventHandler
    public void addHealthBarOnJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        // Add HandcuffsLifeModel is the player is handcuffed
        if(handcuffingController.isHandcuffed(player))
            handcuffsBarController.put(player);
    }
}
