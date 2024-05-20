package com.frahhs.robbing.feature.handcuffing.listener;

import com.frahhs.robbing.RBListener;
import com.frahhs.robbing.feature.handcuffing.mcp.HandcuffsBarController;
import com.frahhs.robbing.feature.handcuffing.event.ToggleHandcuffsEvent;
import com.frahhs.robbing.feature.handcuffing.mcp.Handcuffing;
import com.frahhs.robbing.feature.kidnapping.mcp.KidnappingController;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class HitHandcuffsListener extends RBListener {
    KidnappingController kidnappingController;
    HandcuffsBarController handcuffsBarController;

    public HitHandcuffsListener() {
        kidnappingController = new KidnappingController();
        handcuffsBarController = new HandcuffsBarController();
    }

    @EventHandler
    public void onHandcuff(ToggleHandcuffsEvent e) {
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
    public void onSneak(PlayerToggleSneakEvent e) {
        // Check if player is handcuffed
        if(!Handcuffing.isHandcuffed(e.getPlayer()))
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
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        // Add HandcuffsLifeModel is the player is handcuffed
        if(Handcuffing.isHandcuffed(player))
            handcuffsBarController.put(player);
    }
}
