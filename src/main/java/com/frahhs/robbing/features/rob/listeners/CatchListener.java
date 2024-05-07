package com.frahhs.robbing.features.rob.listeners;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.features.rob.controllers.CatchController;
import com.frahhs.robbing.features.rob.controllers.RobController;
import com.frahhs.robbing.managers.ConfigManager;
import com.frahhs.robbing.managers.MessagesManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Listener class for catching robbers during robbery actions.
 */
public class CatchListener implements Listener {
    private final ConfigManager configManager;

    private final CatchController catchController;

    public CatchListener() {
        configManager = Robbing.getInstance().getConfigManager();

        catchController = new CatchController();
    }

    @EventHandler
    public void catchRobber(EntityDamageByEntityEvent e) {
        // Check if functionality is enabled
        if(!configManager.getBoolean("rob.caught_robber.enabled"))
            return;

        // Check if entities are Players
        if(!(e.getDamager() instanceof Player && e.getEntity() instanceof Player))
            return;

        Player damaged = (Player) e.getEntity();
        Player damager = (Player) e.getDamager();

        // Check if damaged is robbing
        if(!damager.getInventory().getViewers().contains(damaged))
            return;
        // Check if damager is sneaking
        if(!damager.isSneaking())
            return;

        // Do catching things
        e.setCancelled(true);
        catchController.catchRobber(damaged, damager);
    }

    @EventHandler
    public void denyJumpToCaught(PlayerMoveEvent e) {
        // Check if player is caught
        if(!catchController.isCaught(e.getPlayer()))
            return;

        // Check if player is moving
        if(e.getTo() == null)
            return;

        // If player Y location incremented cancel event
        if(!configManager.getBoolean("rob.caught_robber.can_jump")) {
            if (e.getFrom().getY() < e.getTo().getY()) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(Robbing.getInstance().getMessagesManager().getMessage("robbing.cannot_jump_if_caught"));
            }
        }
    }
}
