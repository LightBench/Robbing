package com.frahhs.robbing.feature.rob.listener;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.RobbingListener;
import com.frahhs.robbing.dependencies.worldguard.WorldGuardFlag;
import com.frahhs.robbing.dependencies.worldguard.WorldGuardManager;
import com.frahhs.robbing.feature.handcuffing.mcp.Handcuffing;
import com.frahhs.robbing.feature.rob.event.ItemRobbedEvent;
import com.frahhs.robbing.feature.rob.event.StartRobbingEvent;
import com.frahhs.robbing.feature.rob.mcp.Rob;
import com.frahhs.robbing.feature.rob.mcp.RobController;
import com.frahhs.robbing.util.Cooldown;
import org.bukkit.Bukkit;
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
public class RobListener extends RobbingListener {
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
        if(!config.getBoolean("rob.NPC-rob"))
            if(e.getRightClicked().hasMetadata("NPC"))
                return;

        Player robber = e.getPlayer();
        Player robbed = (Player) e.getRightClicked();

        // Check if robber is sneaking
        if(config.getBoolean("rob.sneak-to-rob"))
            if(!robber.isSneaking())
                return;

        // Check if player is handcuffed
        if(Handcuffing.isHandcuffed(robber))
            return;

        // Check if robbing worldguard flag is deny
        if(Robbing.getInstance().getServer().getPluginManager().getPlugin("WorldGuard") != null) {
            if (WorldGuardFlag.checkStealFlag(robbed)) {
                String message = messages.getMessage("robbing.deny_rob_region");
                robber.sendMessage(message);
                return;
            }
        }

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

        ItemStack item = e.getCurrentItem();

        // Check if slot is null
        if(item == null)
            return;

        Player robber = (Player) e.getWhoClicked();
        Player robbed = null;

        // Retrieve robbed
        for(Player curPlayer : Bukkit.getOnlinePlayers())
            if(curPlayer.getInventory().equals(e.getInventory()))
                robbed = curPlayer;

        // Check if it is a robbing action
        if(!(Rob.isRobbingNow(robber) && Rob.isRobbedNow(robbed)))
            return;
        assert robbed != null;

        // Check rob whitelist and blacklist
        if(!Rob.itemIsRobbable(item)) {
            e.setCancelled(true);
            String message = messages.getMessage("robbing.cant_steal");
            e.getWhoClicked().sendMessage(message);
            return;
        }

        // Call ItemStealCooldown
        ItemRobbedEvent itemStealEvent = new ItemRobbedEvent(item, robber, robbed);
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
                if (robbed.getLocation().distance(robber.getLocation()) >= config.getInt("rob.max-distance")) {
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
        if(!config.getBoolean("rob.blindness-after-robbing.enabled"))
            return;

        // Check if player was robbing and add blindness effect to the target
        int blindness_duration = config.getInt("rob.blindness-after-robbing.duration");
        if (e.getInventory().getType() == InventoryType.PLAYER && !e.getPlayer().getInventory().equals(e.getInventory())) {
            for(Player p : Bukkit.getOnlinePlayers()){
                if(e.getInventory().equals(p.getInventory()))
                    p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * blindness_duration, 1));
            }
        }
    }
}
