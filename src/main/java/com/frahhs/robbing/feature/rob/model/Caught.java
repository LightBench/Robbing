package com.frahhs.robbing.feature.rob.model;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.feature.rob.provider.CaughtProvider;
import com.frahhs.robbing.provider.ConfigProvider;
import com.frahhs.robbing.util.Cooldown;
import org.bukkit.entity.Player;

public class Caught {
    private Caught() {}

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
     * <p>
     * Set to 0 to remove cooldown.
     */
    public static void setCooldown(Player robber, int time) {
        CaughtProvider provider = new CaughtProvider();

        new Thread(() -> {
            try {
                Cooldown cooldown = new Cooldown(System.currentTimeMillis(), time);
                provider.saveCooldown(robber, cooldown);
                Thread.sleep(time * 1000L);
                provider.removeCooldown(robber.getPlayer());
            } catch(InterruptedException v) {
                System.out.println(v.getMessage());
            }
        }).start();
    }

    /**
     * Sets the default cooldown for the caught action.
     */
    public static void setCooldown(Player robber) {
        ConfigProvider config = Robbing.getInstance().getConfigProvider();
        setCooldown(robber, config.getInt("rob.caught_robber.time"));
    }
}
