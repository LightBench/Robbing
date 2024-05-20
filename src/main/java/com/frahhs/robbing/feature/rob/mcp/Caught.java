package com.frahhs.robbing.feature.rob.mcp;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.provider.ConfigProvider;
import com.frahhs.robbing.util.Cooldown;
import org.bukkit.entity.Player;

/**
 * Model class representing the caught action.
 */
public class Caught {
    protected Caught() {}

    /**
     * Checks if a player is currently under a caught cooldown.
     *
     * @param robber The player to check.
     * @return True if the player is under cooldown, otherwise false.
     */
    public static boolean isCaught(Player robber) {
        CaughtProvider provider = new CaughtProvider();
        return provider.haveCooldown(robber);
    }

    /**
     * Retrieves the cooldown for a player.
     *
     * @param robber The player to check.
     * @return The timestamp when the robbing action occurred.
     */
    public static Cooldown getCooldown(Player robber) {
        CaughtProvider provider = new CaughtProvider();
        return provider.getCooldown(robber);
    }

    /**
     * Sets the cooldown for the caught action.
     *
     * @param robber The player to set the cooldown for.
     * @param time   The duration of the cooldown in seconds.
     */
    public static void setCooldown(Player robber, int time) {
        CaughtProvider provider = new CaughtProvider();

        new Thread(() -> {
            try {
                Cooldown cooldown = new Cooldown(System.currentTimeMillis(), time);
                provider.saveCooldown(robber, cooldown);
                Thread.sleep(time * 1000L);
                provider.removeCooldown(robber.getPlayer());
            } catch(InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }).start();
    }

    /**
     * Sets the default cooldown for the caught action.
     *
     * @param robber The player to set the default cooldown for.
     */
    public static void setCooldown(Player robber) {
        ConfigProvider config = Robbing.getInstance().getConfigProvider();
        setCooldown(robber, config.getInt("rob.catch-robber.duration"));
    }
}
