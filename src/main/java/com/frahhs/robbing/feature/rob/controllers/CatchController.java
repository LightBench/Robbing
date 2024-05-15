package com.frahhs.robbing.feature.rob.controllers;

import com.frahhs.robbing.feature.Controller;
import com.frahhs.robbing.feature.rob.model.Caught;
import com.frahhs.robbing.feature.rob.model.Rob;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Controller class for managing caught players.
 */
public class CatchController extends Controller {
    /**
     * Handles catching a robber.
     * TODO: add cooldown on the action bar
     *
     * @param robber The robber who was caught.
     * @param robbed The player who caught the robber.
     */
    public void catchRobber(Player robber, Player robbed) {
        RobController robController = new RobController();
        robController.stopRobbing(robber);

        int caught_robber_time = config.getInt("rob.caught_robber.time");
        int caught_robber_slow_power = config.getInt("rob.caught_robber.slow_power");
        robber.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * caught_robber_time, caught_robber_slow_power));

        // Send message
        robber.sendMessage(messages.getMessage("robbing.caught_robber").replace("{player}", robbed.getDisplayName()));
        robbed.sendMessage(messages.getMessage("robbing.to_catcher").replace("{player}", robber.getDisplayName()));

        // Set cooldown
        Caught.setCooldown(robber);
        Rob.setCooldown(robber);
    }
}
