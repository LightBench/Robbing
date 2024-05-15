package com.frahhs.robbing.feature.rob.listeners;

import com.frahhs.robbing.RBListener;
import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.feature.handcuffing.model.Handcuffing;
import com.frahhs.robbing.feature.rob.controllers.RobController;
import com.frahhs.robbing.feature.rob.events.ItemRobbedEvent;
import com.frahhs.robbing.feature.rob.events.StartRobbingEvent;
import com.frahhs.robbing.feature.rob.model.Rob;
import com.frahhs.robbing.util.Cooldown;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
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
public class RobListener extends RBListener {
    public final RobController robController;

    public RobListener() {
        robController = new RobController();
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent e) {
        // Check if robbing is enabled
        if(!config.getBoolean("rob.enabled"))
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
        if(!config.getBoolean("rob.NPC_robbing"))
            if(e.getRightClicked().hasMetadata("NPC"))
                return;

        Player robber = e.getPlayer();
        Player robbed = (Player) e.getRightClicked();

        // Check if robber is sneaking
        if(config.getBoolean("rob.sneak_to_rob"))
            if(!robber.isSneaking())
                return;

        // Check if player is handcuffed
        if(Handcuffing.isHandcuffed(robber))
            return;

        // TODO: Check if robbing worldguard flag is deny
        /*if(Robbing.getInstance().getServer().getPluginManager().getPlugin("WorldGuard") != null) {
            if (WorldGuardManager.checkStealFlag(thief)) {
                thief.sendMessage(Robbing.prefix + Robbing.getMessageManager().getMessage("robbing", "deny_rob_region"));
                return;
            }
        }*/

        // Check if player is in robbing cooldown
        if(Rob.haveCooldown(robber)){
            Cooldown cooldown = Rob.getCooldown(robber);
            long waitingTime = cooldown.getResidualTime();
            String message = messages.getMessage("general.cooldown").replace("{time}", Long.toString(waitingTime));
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
        if(config.getBoolean("rob.alert"))
            robbed.sendMessage(messages.getMessage("robbing.alert").replace("{thief}", robber.getDisplayName()));
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        // Check if robbing is enabled
        if(!config.getBoolean("rob.enabled"))
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
        if(config.getBoolean("rob.whitelist_enabled")) {
            for (String el : config.getStringList("rob.whitelist_items")) {
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
        for (String el : config.getStringList("rob.denied_items")) {
            if(e.getCurrentItem().getType().equals(Material.getMaterial(el))) {
                cancelRobbing = true;
            }
        }

        // Check whitelisted
        if(config.getBoolean("rob.whitelist_enabled") && !whitelisted)
            cancelRobbing = true;

        // Cancel steal if needed
        if(cancelRobbing) {
            e.setCancelled(true);
            String message = messages.getMessage("robbing.cant_steal");
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
        robController.robbed((Player) e.getWhoClicked());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if(!config.getBoolean("rob.enabled"))
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
            if(!Rob.isRobbingNow(robber))
                return;

            // The viewer must be different by the robbed
            if (!robber.equals(robbed)) {
                // Check how far is the robber
                if (robbed.getLocation().distance(robber.getLocation()) >= config.getInt("rob.max_distance")) {
                    robController.stopRobbing(robber);
                    String message = messages.getMessage("robbing.escaped").replace("{target}", robbed.getDisplayName());
                    robber.sendMessage(message);
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        // Check if robbing is enabled
        if(!config.getBoolean("rob.enabled"))
            return;

        Player robber = (Player)e.getPlayer();

        if(Rob.isRobbingNow(robber))
            robController.stopRobbing(robber);

        // check if blindness effect after robbing is enabled
        if(!config.getBoolean("rob.blindness_after_robbing"))
            return;

        // Check if player was robbing and add blindness effect to the target
        int blindness_duration = config.getInt("rob.blindness_duration");
        if (e.getInventory().getType() == InventoryType.PLAYER && !e.getPlayer().getInventory().equals(e.getInventory())) {
            for(Player p : Bukkit.getOnlinePlayers()){
                if(e.getInventory().equals(p.getInventory()))
                    p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * blindness_duration, 1));
            }
        }
    }
}
