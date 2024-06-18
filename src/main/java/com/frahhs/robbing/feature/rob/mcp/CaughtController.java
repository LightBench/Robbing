package com.frahhs.robbing.feature.rob.mcp;

import com.frahhs.robbing.feature.Controller;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Controller class for managing caught players.
 */
public class CaughtController extends Controller {
    /**
     * Handles catching a robber.
     * TODO: add cooldown on the action bar
     *
     * @param robber The robber who was caught.
     * @param robbed The player who caught the robber.
     */
    public void catchRobber(Player robber, Player robbed) {
        logger.fine("%s has been caught by %s", robber.getName(), robbed.getName());
        RobController robController = new RobController();
        robController.stopRobbing(robber);

        int catchTime = config.getInt("rob.catch-robber.duration");
        int catchSlowPower = config.getInt("rob.catch-robber.slow-power");
        robber.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * catchTime, catchSlowPower));

        // Send message
        robber.sendMessage(messages.getMessage("robbing.catch-robber").replace("{player}", robbed.getDisplayName()));
        robbed.sendMessage(messages.getMessage("robbing.to_catcher").replace("{player}", robber.getDisplayName()));

        // Set cooldown
        Caught.setCooldown(robber);
        Rob.setCooldown(robber);
    }
}
