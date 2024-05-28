package com.frahhs.robbing.feature.handcuffing.listener;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.RobbingListener;
import com.frahhs.robbing.dependencies.DependenciesManager;
import com.frahhs.robbing.dependencies.Dependency;
import com.frahhs.robbing.dependencies.worldguard.WorldGuardFlag;
import com.frahhs.robbing.feature.handcuffing.mcp.Handcuffing;
import com.frahhs.robbing.feature.handcuffing.mcp.HandcuffingController;
import com.frahhs.robbing.item.ItemManager;
import com.frahhs.robbing.item.RobbingMaterial;
import com.frahhs.robbing.util.Cooldown;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class HandcuffingListener extends RobbingListener {
    private final HandcuffingController handcuffingController = new HandcuffingController();

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent e) {
        Player handcuffed;
        Player handcuffer = e.getPlayer();

        String message;

        // Check if handcuffs are enabled
        if(!config.getBoolean("handcuffing.enabled"))
            return;

        // Check if handcuffs target is a player
        if(!(e.getRightClicked() instanceof Player))
            return;

        handcuffed = (Player) e.getRightClicked();

        // Check if the player is using cuffs
        if(!handcuffingController.handcuffsInHand(handcuffer))
            return;

        // Check if player is using main hand
        if (!e.getHand().equals(EquipmentSlot.HAND))
            return;

        // Check if player have permissions
        if(! handcuffer.hasPermission("robbing.cuff") ) {
            message = messages.getMessage("general.no_permission_item");
            handcuffer.sendMessage(message);
            return;
        }

        // Check if worldguard flag is deny
        if(DependenciesManager.haveDependency(Dependency.WORLDGUARD)) {
            if (WorldGuardFlag.checkHandcuffingFlag(handcuffer)) {
                message = messages.getMessage("robbing.deny_region");
                handcuffer.sendMessage(message);
                return;
            }
        }

        // Check if target have permissions to not get handcuffed
        if(handcuffed.hasPermission("robbing.notcuffable")) {
            message = messages.getMessage("handcuffing.not_cuffable");
            handcuffer.sendMessage(message);
            return;
        }

        // Check if the target is already handcuffed
        if(Handcuffing.isHandcuffed(handcuffed)) {
            return;
        }

        // Check if player have handcuffing cooldown
        if(Handcuffing.haveCooldown(handcuffer)) {
            int handcuffing_cooldown = config.getInt("handcuffing.cooldown");

            Cooldown cooldown = Handcuffing.getCooldown(handcuffer);
            long waitingTime = cooldown.getResidualTime();
            message = messages.getMessage("general.cooldown").replace("{time}", Long.toString(waitingTime));
            handcuffer.sendMessage(message);
            return;
        }

        // check if is enabled handcuffing escaping
        if(!config.getBoolean("handcuffing.escape.enabled")) {
            // Handcuff the player
            handcuffer.getInventory().getItemInMainHand().setAmount(handcuffer.getInventory().getItemInMainHand().getAmount() - 1);
            handcuffingController.putHandcuffs(handcuffer, handcuffed);
            return;
        }

        // Handcuffing escaping mechanic
        handcuffingController.escape(handcuffer, handcuffed);
    }

    @EventHandler
    public void onUncuff(PlayerInteractEntityEvent e) {
        if(!(e.getRightClicked() instanceof  Player))
            return;

        if(!e.getHand().equals(EquipmentSlot.HAND))
            return;

        Player handcuffed = (Player) e.getRightClicked();
        Player handcuffer = e.getPlayer();

        String message;

        // Check if handcuffs are enabled
        if(!config.getBoolean("handcuffing.enabled"))
            return;

        // Check if player have permissions
        if(! handcuffer.hasPermission("robbing.uncuff") ) {
            message = messages.getMessage("general.no_permission_item");
            handcuffer.sendMessage(message);
            return;
        }

        // Check if the target is handcuffed
        if(!Handcuffing.isHandcuffed(handcuffed)) {
            return;
        }

        // Check if handcuffer have handcuffs key in main hand, if true remove handcuffs
        ItemManager itemManager = Robbing.getInstance().getItemsManager();
        if(handcuffer.getInventory().getItemInMainHand().isSimilar(itemManager.get(RobbingMaterial.HANDCUFFS_KEY).getItemStack())) {

            // Try to place handcuffs in the inventory
            Map<Integer, ItemStack> map = handcuffer.getInventory().addItem(itemManager.get(RobbingMaterial.HANDCUFFS).getItemStack());

            // If inventory is full drop handcuffs
            if(!map.isEmpty())
                handcuffed.getWorld().dropItemNaturally(handcuffed.getLocation(), itemManager.get(RobbingMaterial.HANDCUFFS_KEY).getItemStack());

            handcuffingController.removeHandcuffs(handcuffed);
        }
    }
}
