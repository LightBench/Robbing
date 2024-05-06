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
    private final ConfigManager configManager = Robbing.getInstance().getConfigManager();
    private final MessagesManager messagesManager = Robbing.getInstance().getMessagesManager();

    private final CatchController catchController = new CatchController();

    @EventHandler
    public void catchThief(EntityDamageByEntityEvent e) {
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
        RobController robController = new RobController();
        robController.stopRobbing(damaged);
        e.setCancelled(true);
        int caught_robber_time = configManager.getInt("rob.caught_robber.time");
        int caught_robber_slow_power = configManager.getInt("rob.caught_robber.slow_power");
        damaged.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * caught_robber_time, caught_robber_slow_power));

        damaged.sendMessage(messagesManager.getMessage("robbing.caught_robber").replace("{player}", damager.getDisplayName()));
        damager.sendMessage(messagesManager.getMessage("robbing.to_catcher").replace("{player}", damaged.getDisplayName()));

        // Add to the caught list for the configured time
        new Thread(() -> {
            try {
                catchController.addCaught(damaged);
                Thread.sleep(caught_robber_time * 1000L);
                catchController.removeCaught(damaged);
            } catch(InterruptedException v) {
                Robbing.getInstance().getRBLogger().error("Unexpected error, send the following stacktrace to our staff: https://discord.gg/Hh9zMQnWvW.");
                Robbing.getInstance().getRBLogger().error(v.toString());
            }
        }).start();
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
