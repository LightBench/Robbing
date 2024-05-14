package com.frahhs.robbing.feature.handcuffing.controller;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.feature.BaseController;
import com.frahhs.robbing.feature.handcuffing.event.ToggleHandcuffsEvent;
import com.frahhs.robbing.feature.handcuffing.model.Handcuffing;
import com.frahhs.robbing.feature.kidnapping.controllers.KidnappingController;
import com.frahhs.robbing.feature.kidnapping.models.Kidnapping;
import com.frahhs.robbing.item.RobbingMaterial;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Controller class for managing handcuffing actions.
 */
public class HandcuffingController extends BaseController {

    /**
     * Default constructor for HandcuffingController.
     */
    public HandcuffingController() {}

    /**
     * Puts handcuffs on a player.
     *
     * @param handcuffer The player who put the handcuffs.
     * @param handcuffed The player to whom put the handcuffs.
     * @param silent Indicates whether to send messages or not.
     */
    public Handcuffing putHandcuffs(Player handcuffer, Player handcuffed, boolean silent) {
        Handcuffing handcuffing = new Handcuffing(handcuffer, handcuffed);

        // Call toggle handcuffed event in server main thread
        Bukkit.getScheduler().runTask(Robbing.getPlugin(Robbing.class), () -> {
            ToggleHandcuffsEvent toggleHandcuffsEvent = new ToggleHandcuffsEvent(handcuffed, handcuffer, true);
            Bukkit.getPluginManager().callEvent(toggleHandcuffsEvent);

            // Check if event is cancelled
            if(toggleHandcuffsEvent.isCancelled())
                return;

            // Handcuffing stuff
            handcuffing.save();
            handcuffed.setAllowFlight(true);
            handcuffed.setGliding(false);

            // Send handcuffing messages...
            if(!silent) {
                String message;
                message = messages.getMessage("handcuffing.cuffed").replace("{player}", handcuffer.getDisplayName());
                handcuffed.sendMessage(message);
                message = messages.getMessage("handcuffing.cuff").replace("{target}", handcuffed.getDisplayName());
                handcuffer.sendMessage(message);
            }

            // Add handcuffing cooldown to handcuffer
            Handcuffing.setCooldown(handcuffer);
        });

        return handcuffing;
    }

    /**
     * Puts handcuffs on a player.
     *
     * @param handcuffer The player who put the handcuffs.
     * @param handcuffed The player to whom put the handcuffs.
     */
    public Handcuffing putHandcuffs(Player handcuffer, Player handcuffed) {
        return putHandcuffs(handcuffer, handcuffed, false);
    }

    /**
     * Removes handcuffs from a player.
     *
     * @param handcuffed The player from whom to remove the handcuffs.
     * @param silent Indicates whether to send messages or not.
     */
    public void removeHandcuffs(Player handcuffed, boolean silent) {
        Handcuffing handcuffing = Handcuffing.getFromHandcuffed(handcuffed);
        final Player handcuffer = handcuffing.getHandcuffer();

        Bukkit.getScheduler().runTask(Robbing.getPlugin(Robbing.class), () -> {
            ToggleHandcuffsEvent toggleHandcuffsEvent = new ToggleHandcuffsEvent(handcuffed, handcuffer, false);
            Bukkit.getPluginManager().callEvent(toggleHandcuffsEvent);

            // Check if event is cancelled
            if(toggleHandcuffsEvent.isCancelled())
                return;

            // Handcuffing stuff
            handcuffing.remove();
            handcuffed.setAllowFlight(false);

            String message;
            // Send handcuffing messages...
            if(!silent) {
                message = messages.getMessage("handcuffing.uncuffed").replace("{player}", handcuffer.getDisplayName());
                handcuffed.sendMessage(message);
                message = messages.getMessage("handcuffing.uncuff").replace("{target}", handcuffed.getDisplayName());
                handcuffer.sendMessage(message);
            }

            // Check also if the target is in following mode and remove it
            KidnappingController kidnappingController = new KidnappingController();
            if(Kidnapping.isKidnapped(handcuffed)) {
                kidnappingController.free(handcuffed);
                if(!silent) {
                    message = messages.getMessage("follow.make_unfollow_cuffed");
                    message = message.replace("{target}", handcuffed.getDisplayName());
                    handcuffer.sendMessage(message);
                }
            }
        });
    }

    /**
     * Removes handcuffs from a player.
     *
     * @param handcuffed The player from whom to remove the handcuffs.
     */
    public void removeHandcuffs(Player handcuffed) {
        removeHandcuffs(handcuffed, false);
    }

    /**
     * Checks if a player is currently using handcuffs.
     *
     * @param player The player to check.
     * @return True if the player is using handcuffs, otherwise false.
     */
    public boolean handcuffsInHand(Player player) {
        // Handle if player have more than 1 handcuffs in hand
        int amount = player.getInventory().getItemInMainHand().getAmount();

        ItemStack handcuffsItemStack = Robbing.getInstance().getItemsManager().get(RobbingMaterial.HANDCUFFS).getItemStack();
        handcuffsItemStack.setAmount(amount);

        return player.getInventory().getItemInMainHand().equals(handcuffsItemStack);
    }

    /**
     * Allows a handcuffed player to escape from the handcuffs.
     *
     * @param handcuffer The player who handcuffed.
     * @param handcuffed The player who is handcuffed.
     */
    public void escape(Player handcuffer, Player handcuffed) {
        int delay = config.getInt("handcuffing.escape.delayed_handcuffing");
        int distance = config.getInt("handcuffing.escape.distance");
        int handcuffs_cd = config.getInt("handcuffing.cooldown");

        String time_to_escape = messages.getMessage("actionbar.time_to_escape");
        String handcuffing = messages.getMessage("actionbar.handcuffing");
        String escaped = messages.getMessage("actionbar.escaped");
        String handcuffing_failed = messages.getMessage("actionbar.handcuffing_failed");
        String handcuffed_msg = messages.getMessage("actionbar.handcuffed");

        // Wait the delay time to use handcuffs again
        Handcuffing.setCooldown(handcuffer, delay + handcuffs_cd);

        new Thread(() -> {
            try {
                for(int i = delay; i >= 1; i--) {
                    TextComponent time_to_escape_tc = new TextComponent(time_to_escape.replace("{time}", Integer.toString(i)));
                    TextComponent handcuffing_tc = new TextComponent(handcuffing.replace("{time}", Integer.toString(i)));
                    TextComponent escaped_tc = new TextComponent(escaped.replace("{time}", Integer.toString(i)));
                    TextComponent handcuffing_failed_tc = new TextComponent(handcuffing_failed.replace("{time}", Integer.toString(i)));

                    handcuffed.spigot().sendMessage(ChatMessageType.ACTION_BAR, time_to_escape_tc);
                    handcuffer.spigot().sendMessage(ChatMessageType.ACTION_BAR, handcuffing_tc);
                    Thread.sleep(1000L);
                    if(handcuffer.getLocation().distance(handcuffed.getLocation()) > distance) {
                        handcuffed.spigot().sendMessage(ChatMessageType.ACTION_BAR, escaped_tc);
                        handcuffer.spigot().sendMessage(ChatMessageType.ACTION_BAR, handcuffing_failed_tc);
                        return;
                    }
                }
                // If escaping attempt fails, handcuff the player again
                TextComponent handcuffed_msg_tc = new TextComponent(handcuffed_msg);
                handcuffed.spigot().sendMessage(ChatMessageType.ACTION_BAR, handcuffed_msg_tc);
                handcuffer.spigot().sendMessage(ChatMessageType.ACTION_BAR, handcuffed_msg_tc);

                putHandcuffs(handcuffer, handcuffed);
            } catch(InterruptedException v) {
                System.out.println(v.getMessage());
            }
        }).start();
    }
}
