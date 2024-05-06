package com.frahhs.robbing.features.handcuffing.listeners;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.features.handcuffing.controller.HandcuffingController;
import com.frahhs.robbing.features.handcuffing.models.HandcuffingModel;
import com.frahhs.robbing.managers.ConfigManager;
import com.frahhs.robbing.managers.MessagesManager;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.sql.Timestamp;

public class HandcuffingListener implements Listener {
    private final ConfigManager configManager = Robbing.getInstance().getConfigManager();
    private final MessagesManager messagesManager = Robbing.getInstance().getMessagesManager();

    private final HandcuffingController handcuffingController = new HandcuffingController();

    @EventHandler
    public void handcuffsMechanics(PlayerInteractEntityEvent e) {
        Player handcuffed;
        Player handcuffer = e.getPlayer();

        String message;

        // Check if handcuffs are enabled
        if(!configManager.getBoolean("handcuffing.enabled"))
            return;

        // Check if handcuffs target is a player
        if(!(e.getRightClicked() instanceof Player))
            return;

        handcuffed = (Player) e.getRightClicked();

        // Check if the player is using cuffs
        if(!handcuffingController.isUsingHandcuffs(e.getPlayer()))
            return;

        // Check if player is using main hand
        if (!e.getHand().equals(EquipmentSlot.HAND))
            return;

        // Check if player have permissions
        if(! handcuffer.hasPermission("robbing.cuff") ) {
            message = messagesManager.getMessage("general.no_permission_item");
            e.getPlayer().sendMessage(message);
            return;
        }

        // Check if target have permissions to not get handcuffed
        if(handcuffed.hasPermission("robbing.notcuffable")) {
            message = messagesManager.getMessage("handcuffing.not_cuffable");
            handcuffer.sendMessage(message);
            return;
        }

        // Check if the target is already handcuffed, soo remove handcuffs
        if(handcuffingController.isHandcuffed(handcuffed)) {
            handcuffingController.removeHandcuffs(handcuffed);
            return;
        }

        // Check if player have handcuffing cooldown
        if(HandcuffingController.handcuffingCooldown.containsKey(e.getPlayer())) {
            int handcuffing_cooldown = configManager.getInt("handcuffing.cooldown");
            long waitingTime = handcuffing_cooldown - ((System.currentTimeMillis() - HandcuffingController.handcuffingCooldown.get(e.getPlayer())) / 1000 );
            message = messagesManager.getMessage("general.cooldown").replace("{time}", Long.toString(waitingTime));
            handcuffer.sendMessage(message);
            return;
        }

        // check if is enabled handcuffing escaping
        if(!configManager.getBoolean("handcuffing.try_escape.enabled")) {
            // Handcuff the player
            HandcuffingModel handcuffingModel = new HandcuffingModel(handcuffed, handcuffer, new Timestamp(System.currentTimeMillis()));
            handcuffingController.putHandcuffs(handcuffingModel);
            return;
        }

        /* TODO: Handcuffing escaping mechanic */

        // Check if player is already using handcuffs
        /*if(handcuffingController.isUsingHandcuffs(handcuffer))
            return;

        new Thread(() -> {
            try {
                Handcuffs.isUsingHandcuffs.add(handcuffer);
                Thread.sleep(ConfigManager.handcuffing_try_escape_delayed_handcuffing * 1000L);
                Handcuffs.isUsingHandcuffs.remove(handcuffer);
            } catch(InterruptedException v) {
                System.out.println(v.getMessage());
            }
        }).start();

        // Check position after the configured time to check if far enough to escape
        new Thread(() -> {
            try {
                for(int i = ConfigManager.handcuffing_try_escape_delayed_handcuffing; i >= 1; i--) {
                    handcuffed.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§2" + Robbing.getMessageManager().getMessage("actionbar", "time_to_escape").replace("{time}", Integer.toString(i))));//("§2 You have " + i + " to escapefsdfdsf..."));
                    handcuffer.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§2" + Robbing.getMessageManager().getMessage("actionbar", "handcuffing").replace("{time}", Integer.toString(i))));
                    Thread.sleep(1000L);
                    if(handcuffer.getLocation().distance(handcuffed.getLocation()) > ConfigManager.handcuffing_try_escape_distance) {
                        handcuffed.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§2" + Robbing.getMessageManager().getMessage("actionbar", "escaped").replace("{time}", Integer.toString(i))));
                        handcuffer.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§4" + Robbing.getMessageManager().getMessage("actionbar", "handcuffing_failed").replace("{time}", Integer.toString(i))));
                        return;
                    }
                }
                // If escaping attempt fail handcuff
                handcuffed.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§2" + Robbing.getMessageManager().getMessage("actionbar", "handcuffed")));
                handcuffer.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§2" + Robbing.getMessageManager().getMessage("actionbar", "handcuffed")));
                Handcuffs.putHandcuffs(handcuffer, handcuffed);
            } catch(InterruptedException v) {
                System.out.println(v.getMessage());
            }
        }).start();*/
    }

    @EventHandler
    public void handCuffsMechanics(PlayerLeashEntityEvent e) {
        // Remove vanilla leash events to cuffs
        if(handcuffingController.isUsingHandcuffs(e.getPlayer())) {
            e.setCancelled(true);
        }
    }
}
