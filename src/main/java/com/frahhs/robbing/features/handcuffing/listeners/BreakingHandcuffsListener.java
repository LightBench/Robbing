package com.frahhs.robbing.features.handcuffing.listeners;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.features.handcuffing.controller.HandcuffingController;
import com.frahhs.robbing.features.handcuffing.controller.KidnappingController;
import com.frahhs.robbing.features.handcuffing.events.ToggleHandcuffsEvent;
import com.frahhs.robbing.features.handcuffing.models.HandcuffingModel;
import com.frahhs.robbing.features.handcuffing.models.HandcuffsLifeModel;
import com.frahhs.robbing.managers.ConfigManager;
import com.frahhs.robbing.managers.MessagesManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class BreakingHandcuffsListener implements Listener {
    private final ConfigManager configManager = Robbing.getInstance().getConfigManager();
    private final MessagesManager messagesManager = Robbing.getInstance().getMessagesManager();

    public BreakingHandcuffsListener() {
        HandcuffsLifeModel.putAllHandcuffsLifeModel();
    }

    @EventHandler
    public void PlaceHandcuffsLifeModel(ToggleHandcuffsEvent e) {
        if(e.isCancelled())
            return;

        // If it is putting on handcuffs add HealthBar, else remove it
        setHandcuffsLifeModel(e.getHandcuffed(), e.isPuttingOn());

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
        HandcuffingController handcuffingController = new HandcuffingController();
        KidnappingController kidnappingController = new KidnappingController();
        HandcuffsLifeModel bar = HandcuffsLifeModel.getBarFromPlayer(handcuffed);
        if(bar.crack()) {
            // Remove handcuffs
            handcuffingController.removeHandcuffs(handcuffed, true);

            // Free handcuffed if was kidnapped
            kidnappingController.free(handcuffed);

            // Remove bar
            bar.removeHandcuffsLifeModel(handcuffed);

            // Send message
            String message = messagesManager.getMessage("handcuffing.broken_handcuffs");
            handcuffed.sendMessage(message);
        }
    }

    @EventHandler
    public void addHealthBarOnJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        // Add HandcuffsLifeModel is the player is handcuffed
        if(HandcuffingModel.isHandcuffed(p))
            setHandcuffsLifeModel(p, true);
    }

    private void setHandcuffsLifeModel(Player handcuffed, boolean put) {
        HandcuffsLifeModel healthBar = new HandcuffsLifeModel();

        // Put or remove HandcuffsLifeModel
        if(put) {
            healthBar.putHandcuffsLifeModel(handcuffed);
        } else {
            healthBar.removeHandcuffsLifeModel(handcuffed);
        }
    }
}
