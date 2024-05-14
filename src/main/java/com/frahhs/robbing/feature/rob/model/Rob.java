package com.frahhs.robbing.feature.rob.model;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.feature.BaseModel;
import com.frahhs.robbing.feature.rob.provider.RobProvider;
import com.frahhs.robbing.provider.ConfigProvider;
import com.frahhs.robbing.util.Cooldown;
import org.bukkit.entity.Player;

public class Rob extends BaseModel {
    private final Player robber;
    private final Player robbed;

    private final RobProvider provider;

    public Rob(Player robber, Player robbed) {
        this.robber = robber;
        this.robbed = robbed;

        this.provider = new RobProvider();
    }

    public Player getRobber() {
        return robber;
    }

    public Player getRobbed() {
        return robbed;
    }

    public static boolean isRobbingNow(Player robber) {
        RobProvider provider = new RobProvider();
        return provider.isRobbingNow(robber);
    }

    public static boolean isRobbedNow(Player robbed) {
        RobProvider provider = new RobProvider();
        return provider.isRobbedNow(robbed);
    }

    public static void setRobbingNow(Player robber, Player robbed) {
        RobProvider provider = new RobProvider();
        provider.saveRobbingNow(robber, robbed);
    }

    public static void removeRobbingNow(Player robber) {
        RobProvider provider = new RobProvider();
        provider.removeRobbingNow(robber);
    }

    /**
     * Checks if a player is currently under a handcuffing cooldown.
     *
     * @param handcuffer The player to check.
     * @return True if the player is under cooldown, otherwise false.
     */
    public static boolean haveCooldown(Player handcuffer) {
        RobProvider provider = new RobProvider();
        return provider.haveCooldown(handcuffer);
    }

    /**
     * Retrieves the cooldown for a player.
     *
     * @param handcuffer The player to check.
     * @return The timestamp when the robbing action occurred.
     */
    public static Cooldown getCooldown(Player handcuffer) {
        RobProvider provider = new RobProvider();
        return provider.getCooldown(handcuffer);
    }

    /**
     * Sets the cooldown for the robbing action.
     * <p>
     * Set to 0 to remove cooldown.
     */
    public static void setCooldown(Player handcuffer, int time) {
        RobProvider provider = new RobProvider();

        new Thread(() -> {
            try {
                Cooldown cooldown = new Cooldown(System.currentTimeMillis(), time);
                provider.saveCooldown(handcuffer, cooldown);
                Thread.sleep(time * 1000L);
                provider.removeCooldown(handcuffer.getPlayer());
            } catch(InterruptedException v) {
                System.out.println(v.getMessage());
            }
        }).start();
    }

    /**
     * Sets the default cooldown for the robbing action.
     */
    public static void setCooldown(Player handcuffer) {
        ConfigProvider config = Robbing.getInstance().getConfigProvider();
        setCooldown(handcuffer, config.getInt("rob.steal_cooldown"));
    }
}
