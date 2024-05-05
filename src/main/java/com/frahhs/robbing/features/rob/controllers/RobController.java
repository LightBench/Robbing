package com.frahhs.robbing.features.rob.controllers;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.managers.ConfigManager;
import com.frahhs.robbing.managers.MessagesManager;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller class for managing robbery actions.
 */
public class RobController {

    public static List<Player> isRobbingNowList = new ArrayList<>();
    public static Map<Player, Long> robbing_cooldown_list = new HashMap<>();

    private final ConfigManager configManager;
    private final MessagesManager messagesManager;

    /**
     * Constructor for RobController.
     */
    public RobController() {
        configManager = Robbing.getInstance().getConfigManager();
        messagesManager = Robbing.getInstance().getMessagesManager();
    }

    /**
     * Starts the robbery action between a robber and a robbed player.
     *
     * @param robber The player initiating the robbery.
     * @param robbed The player being robbed.
     */
    public void startRobbing(Player robber, Player robbed) {
        setIsRobbingNow(robber, true);
        robber.openInventory(robbed.getInventory());
    }

    /**
     * Stops the ongoing robbery action for a player.
     *
     * @param robber The player ending the robbery.
     */
    public void stopRobbing(Player robber) {
        if (isRobbingNow(robber)) {
            setIsRobbingNow(robber, false);
            robber.closeInventory();
        }
    }

    /**
     * Checks if a player is currently robbing.
     *
     * @param player The player to check.
     * @return True if the player is currently robbing, false otherwise.
     */
    public boolean isRobbingNow(Player player) {
        return isRobbingNowList.contains(player);
    }

    /**
     * Sets the robbing status for a player.
     *
     * @param player       The player to set the robbing status for.
     * @param isRobbingNow The robbing status to set.
     */
    public void setIsRobbingNow(Player player, boolean isRobbingNow) {
        if (isRobbingNow && !isRobbingNowList.contains(player))
            isRobbingNowList.add(player);
        else if (!isRobbingNow)
            isRobbingNowList.remove(player);
    }

    /**
     * Checks if a player has a robbery cooldown.
     *
     * @param player The player to check.
     * @return True if the player has a cooldown, false otherwise.
     */
    public boolean haveCooldown(Player player) {
        return robbing_cooldown_list.containsKey(player);
    }

    /**
     * Gets the remaining cooldown time for a player.
     *
     * @param player The player to get the cooldown for.
     * @return The remaining cooldown time in seconds.
     */
    public long getCooldown(Player player) {
        if (haveCooldown(player)) {
            int steal_cooldown = configManager.getInt("rob.steal_cooldown");
            return steal_cooldown - ((Instant.now().toEpochMilli() - robbing_cooldown_list.get(player)) / 1000);
        }
        return 0L;
    }

    /**
     * Adds a player to the robbery cooldown list and removes them after the cooldown period.
     *
     * @param player The player to add to the cooldown list.
     */
    public void addRobber(Player player) {
        new Thread(() -> {
            try {
                robbing_cooldown_list.put(player, Instant.now().toEpochMilli());
                int steal_cooldown = configManager.getInt("rob.steal_cooldown");
                Thread.sleep(steal_cooldown * 1000L);
                robbing_cooldown_list.remove(player);
            } catch (InterruptedException v) {
                Robbing.getInstance().getRBLogger().error("Unexpected error, send the following stacktrace to our staff: https://discord.gg/Hh9zMQnWvW.");
                Robbing.getInstance().getRBLogger().error(v.toString());
            }
        }).start();
    }
}
