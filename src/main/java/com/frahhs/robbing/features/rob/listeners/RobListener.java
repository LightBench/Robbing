package com.frahhs.robbing.features.rob.listeners;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.features.handcuffing.controller.HandcuffingController;
import com.frahhs.robbing.features.handcuffing.models.HandcuffingModel;
import com.frahhs.robbing.features.rob.controllers.RobController;
import com.frahhs.robbing.features.rob.events.ItemRobbedEvent;
import com.frahhs.robbing.features.rob.events.StartRobbingEvent;
import com.frahhs.robbing.managers.ConfigManager;
import com.frahhs.robbing.managers.MessagesManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Listener class for robbery-related events.
 */
public class RobListener implements Listener {
    public final RobController robController = new RobController();

    private final ConfigManager configManager = Robbing.getInstance().getConfigManager();
    private final MessagesManager messagesManager = Robbing.getInstance().getMessagesManager();

    @EventHandler
    public void doSteal(PlayerInteractEntityEvent e) {
        // Check if robbing is enabled
        if(!configManager.getBoolean("rob.enabled"))
            return;

        // Check if player have permissions
        if(!e.getPlayer().hasPermission("robbing.steal"))
            return;

        // Check if player is using main hand
        if(e.getHand() == EquipmentSlot.HAND)
            return;

        // Check if target is a Player
        if(!(e.getRightClicked() instanceof Player))
            return;

        // Check if target have not stealable permissions
        if(e.getRightClicked().hasPermission("robbing.notstealable"))
            return;

        // Check if target is an NPC
        if(!configManager.getBoolean("rob.NPC_robbing"))
            if(e.getRightClicked().hasMetadata("NPC"))
                return;

        Player robber = e.getPlayer();
        Player robbed = (Player) e.getRightClicked();

        // Check if robber is sneaking
        if(configManager.getBoolean("rob.sneak_to_rob"))
            if(!robber.isSneaking())
                return;

        // Check if player is handcuffed
        if(HandcuffingModel.isHandcuffed(robber))
            return;

        // TODO: Check if robbing worldguard flag is deny
        /*if(Robbing.getInstance().getServer().getPluginManager().getPlugin("WorldGuard") != null) {
            if (WorldGuardManager.checkStealFlag(thief)) {
                thief.sendMessage(Robbing.prefix + Robbing.getMessageManager().getMessage("robbing", "deny_rob_region"));
                return;
            }
        }*/

        // Check if player is in robbing cooldown
        if(robController.haveCooldown(robber)){
            long waitingTime = robController.getCooldown(robber);
            String message = messagesManager.getMessage("general.cooldown").replace("{time}", Long.toString(waitingTime));
            robber.sendMessage(message);
            return;
        }

        // Call StartStealEvent
        StartRobbingEvent startRobbingEvent = new StartRobbingEvent(robber, robbed);
        Bukkit.getPluginManager().callEvent(startRobbingEvent);

        // Check if event is cancelled
        if(startRobbingEvent.isCancelled())
            return;

        // Open target inventory
        robController.startRobbing(robber, robbed);

        // Send robbing alert to the target
        if(configManager.getBoolean("rob.alert"))
            robbed.sendMessage(messagesManager.getMessage("robbing.alert").replace("{thief}", robber.getDisplayName()));
    }

    @EventHandler
    public void stolenItem(InventoryClickEvent e) {
        // Check if robbing is enabled
        if(!configManager.getBoolean("rob.enabled"))
            return;

        // Check if Inventory is player type and at least 2 people are watching it
        if (!(e.getInventory().getType() == InventoryType.PLAYER) || !(e.getViewers().size() >= 2))
            return;

        // Check if item is picked from target inventory
        if (!e.getInventory().equals(e.getClickedInventory()))
            return;

        // Check if is in target inventory
        if(e.getSlot() >= 36)
            return;

        // Check if slot is null
        if(e.getCurrentItem() == null)
            return;

        boolean cancelRobbing = false;
        boolean whitelisted = false;

        // If whitelist enabled and Item is not in whitelist remove
        if(configManager.getBoolean("rob.whitelist_enabled")) {
            for (String el : configManager.getStringList("rob.whitelist_items")) {
                if(e.getCurrentItem().getType().equals(Material.getMaterial(el))) {
                    whitelisted = true;
                }
            }
        }

        // Instance main feature variables
        ItemStack stolenItem = e.getCurrentItem();
        Player robber = (Player) e.getWhoClicked();
        Player robbed = null;

        for(Player curPlayer : Bukkit.getOnlinePlayers())
            if(curPlayer.getInventory().equals(e.getInventory()))
                robbed = curPlayer;

        // Check if target is null (Probably never)
        if(robbed == null) {
            Robbing.getInstance().getRBLogger().unexpectedError("U8PH3N");
            return;
        }

        // If current Item is the same of denied, cancel action
        for (String el : configManager.getStringList("rob.denied_items")) {
            if(e.getCurrentItem().getType().equals(Material.getMaterial(el))) {
                cancelRobbing = true;
            }
        }

        // Check whitelisted
        if(configManager.getBoolean("rob.whitelist_enabled") && !whitelisted)
            cancelRobbing = true;

        // Cancel steal if needed
        if(cancelRobbing) {
            e.setCancelled(true);
            String message = messagesManager.getMessage("robbing.cant_steal");
            e.getWhoClicked().sendMessage(message);
        }

        // Call ItemStealCooldown
        ItemRobbedEvent itemStealEvent = new ItemRobbedEvent(stolenItem, robber, robbed);
        Bukkit.getPluginManager().callEvent(itemStealEvent);

        // Check if event is cancelled
        if(itemStealEvent.isCancelled()) {
            e.setCancelled(true);
            return;
        }

        // Add to robbing cooldown
        robController.addRobber((Player) e.getWhoClicked());
    }

    @EventHandler
    public void runaway(PlayerMoveEvent e) {
        if(!configManager.getBoolean("rob.enabled"))
            return;

        Player robbed = e.getPlayer();

        // Check if almost two players are watching the inventory
        if(!(robbed.getInventory().getViewers().size() >= 2))
            return;


        // Iterate all viewers
        for (HumanEntity he : robbed.getInventory().getViewers()) {
            Player robber = Bukkit.getPlayer(he.getName());

            // Check if robber exist
            if(robber == null)
                return;

            // Check if the robber is robbing now
            if(!robController.isRobbingNow(robber))
                return;

            // The viewer must be different by the robbed
            if (!robber.equals(robbed)) {
                // Check how far is the robber
                if (robbed.getLocation().distance(robber.getLocation()) >= configManager.getInt("rob.max_distance")) {
                    robController.stopRobbing(robber);
                    String message = messagesManager.getMessage("robbing.escaped").replace("{target}", robbed.getDisplayName());
                    robber.sendMessage(message);
                    return;
                }
            }
        }
    }

    @EventHandler
    public void endRobbing(InventoryCloseEvent e) {
        // Check if robbing is enabled
        if(!configManager.getBoolean("rob.enabled"))
            return;

        Player robber = (Player)e.getPlayer();

        if(robController.isRobbingNow(robber))
            robController.stopRobbing(robber);

        // check if blindness effect after robbing is enabled
        if(!configManager.getBoolean("rob.blindness_after_robbing"))
            return;

        // Check if player was robbing and add blindness effect to the target
        int blindness_duration = configManager.getInt("rob.blindness_duration");
        if (e.getInventory().getType() == InventoryType.PLAYER && !e.getPlayer().getInventory().equals(e.getInventory())) {
            for(Player p : Bukkit.getOnlinePlayers()){
                if(e.getInventory().equals(p.getInventory()))
                    p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * blindness_duration, 1));
            }
        }
    }
}
