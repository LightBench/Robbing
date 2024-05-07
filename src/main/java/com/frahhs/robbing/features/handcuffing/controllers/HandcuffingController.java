package com.frahhs.robbing.features.handcuffing.controllers;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.features.BaseController;
import com.frahhs.robbing.features.handcuffing.events.ToggleHandcuffsEvent;
import com.frahhs.robbing.features.handcuffing.models.CooldownModel;
import com.frahhs.robbing.features.handcuffing.models.HandcuffingModel;
import com.frahhs.robbing.features.kidnapping.controllers.KidnappingController;
import com.frahhs.robbing.features.kidnapping.models.KidnappingModel;
import com.frahhs.robbing.items.RBMaterial;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.Timestamp;

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
    public void putHandcuffs(Player handcuffer, Player handcuffed, boolean silent) {
        HandcuffingModel handcuffingModel = new HandcuffingModel(handcuffer, handcuffed, new Timestamp(System.currentTimeMillis()));

        // Call toggle handcuffed event in server main thread
        Bukkit.getScheduler().runTask(Robbing.getPlugin(Robbing.class), () -> {
            ToggleHandcuffsEvent toggleHandcuffsEvent = new ToggleHandcuffsEvent(handcuffed, handcuffer, true);
            Bukkit.getPluginManager().callEvent(toggleHandcuffsEvent);

            // Check if event is cancelled
            if(toggleHandcuffsEvent.isCancelled())
                return;

            // Handcuffing stuff
            handcuffingModel.save();
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
            setCooldown(handcuffer);
        });
    }

    /**
     * Puts handcuffs on a player.
     *
     * @param handcuffer The player who put the handcuffs.
     * @param handcuffed The player to whom put the handcuffs.
     */
    public void putHandcuffs(Player handcuffer, Player handcuffed) {
        putHandcuffs(handcuffer, handcuffed, false);
    }

    /**
     * Removes handcuffs from a player.
     *
     * @param handcuffed The player from whom to remove the handcuffs.
     * @param silent Indicates whether to send messages or not.
     */
    public void removeHandcuffs(Player handcuffed, boolean silent) {
        HandcuffingModel handcuffingModel = HandcuffingModel.getFromHandcuffed(handcuffed);
        final Player handcuffer = handcuffingModel.getHandcuffer();

        Bukkit.getScheduler().runTask(Robbing.getPlugin(Robbing.class), () -> {
            ToggleHandcuffsEvent toggleHandcuffsEvent = new ToggleHandcuffsEvent(handcuffed, handcuffer, false);
            Bukkit.getPluginManager().callEvent(toggleHandcuffsEvent);

            // Check if event is cancelled
            if(toggleHandcuffsEvent.isCancelled())
                return;

            // Handcuffing stuff
            handcuffingModel.remove();
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
            if(KidnappingModel.isKidnapped(handcuffed)) {
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
     * @param p The player to check.
     * @return True if the player is using handcuffs, otherwise false.
     */
    public boolean isUsingHandcuffs(Player p) {
        // Handle if player have more than 1 handcuffs in hand
        int amount = p.getInventory().getItemInMainHand().getAmount();

        ItemStack handcuffsItemStack = Robbing.getInstance().getItemsManager().get(RBMaterial.HANDCUFFS);
        handcuffsItemStack.setAmount(amount);

        return p.getInventory().getItemInMainHand().equals(handcuffsItemStack);
    }

    /**
     * Checks if a player is handcuffed.
     *
     * @param handcuffed The player to check.
     * @return True if the player is handcuffed, otherwise false.
     */
    public boolean isHandcuffed(Player handcuffed) {
        return HandcuffingModel.isHandcuffed(handcuffed);
    }

    /**
     * Gets the player who handcuffed the specified player.
     *
     * @param handcuffed The player who is handcuffed.
     * @return The player who handcuffed the specified player.
     */
    public Player getHandcuffer(Player handcuffed) {
        if(!isHandcuffed(handcuffed))
            return null;

        HandcuffingModel handcuffingModel = HandcuffingModel.getFromHandcuffed(handcuffed);

        assert handcuffingModel != null;

        return handcuffingModel.getHandcuffer();
    }

    /**
     * Checks if a player has a cooldown for handcuffing.
     *
     * @param handcuffer The player to check.
     * @return True if the player has a cooldown, otherwise false.
     */
    public boolean haveCooldown(Player handcuffer) {
        return HandcuffingModel.haveCooldown(handcuffer);
    }

    /**
     * Sets a cooldown for handcuffing for the specified player.
     *
     * @param handcuffer The player for whom to set the cooldown.
     * @param cooldown The cooldown duration in seconds.
     */
    public void setCooldown(Player handcuffer, int cooldown) {
        HandcuffingModel.setCooldown(handcuffer, cooldown);
    }

    /**
     * Sets a default cooldown for handcuffing for the specified player.
     *
     * @param handcuffer The player for whom to set the cooldown.
     */
    public void setCooldown(Player handcuffer) {
        HandcuffingModel.setCooldown(handcuffer);
    }

    /**
     * Gets the cooldown time for handcuffing of a player.
     *
     * @param handcuffer The player to check.
     * @return The CooldownModel containing the cooldown time.
     */
    public CooldownModel getCooldown(Player handcuffer) {
        return HandcuffingModel.getCooldown(handcuffer);
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
        setCooldown(handcuffer, delay + handcuffs_cd);

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
