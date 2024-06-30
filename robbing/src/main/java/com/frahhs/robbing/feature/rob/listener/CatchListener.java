package com.frahhs.robbing.feature.rob.listener;

import com.frahhs.lightlib.LightListener;
import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.feature.rob.mcp.Caught;
import com.frahhs.robbing.feature.rob.mcp.CaughtController;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Listener class for catching robbers during robbery actions.
 */
public class CatchListener extends LightListener {
    private final CaughtController caughtController;

    public CatchListener() {
        caughtController = new CaughtController();
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        // Check if functionality is enabled
        if(!config.getBoolean("rob.catch-robber.enabled"))
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

        if(!damager.hasPermission("robbing.catch")) {
            String message = messages.getMessage("general.no_permissions");
            damager.sendMessage(message);
            return;
        }

        // Do catching things
        caughtController.catchRobber(damaged, damager);
        e.setCancelled(true);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        // Check if player is caught
        if(!Caught.isCaught(e.getPlayer()))
            return;

        // Check if player is moving
        if(e.getTo() == null)
            return;

        // If player Y location incremented cancel event
        if(!config.getBoolean("rob.catch-robber.can-jump")) {
            if (e.getFrom().getY() < e.getTo().getY()) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(Robbing.getInstance().getMessagesProvider().getMessage("robbing.cannot_jump_if_caught"));
            }
        }
    }
}
