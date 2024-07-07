package com.frahhs.robbing.feature.rob.mcp;

import com.frahhs.lightlib.LightPlugin;
import com.frahhs.lightlib.feature.LightModel;
import com.frahhs.lightlib.provider.ConfigProvider;
import com.frahhs.lightlib.util.Cooldown;
import com.frahhs.robbing.Robbing;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * LightModel class representing the caught action.
 */
public class Caught  extends LightModel {
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

        Bukkit.getScheduler().runTaskAsynchronously(Robbing.getPlugin(Robbing.class), () -> {
            for(int i = time*10; i > 0; i--) {
                try {
                    String message = LightPlugin.getMessagesProvider().getMessage("robbing.caught_actionbar_cooldown");
                    float resTime = ((float)(i - 1)) / 10 ;
                    robber.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message.replace("{time}", Float.toString(resTime))));
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Bukkit.getScheduler().runTaskAsynchronously(Robbing.getPlugin(Robbing.class), () -> {
            try {
                Cooldown cooldown = new Cooldown(System.currentTimeMillis(), time);
                provider.saveCooldown(robber, cooldown);
                Thread.sleep(time * 1000L);
                provider.removeCooldown(robber.getPlayer());
            } catch(InterruptedException e) {
                LightPlugin.getLightLogger().error("Error handling caught cooldown for %s, %s", robber.getName(), e);
            }
        });
    }

    /**
     * Sets the default cooldown for the caught action.
     *
     * @param robber The player to set the default cooldown for.
     */
    public static void setCooldown(Player robber) {
        ConfigProvider config = LightPlugin.getConfigProvider();
        setCooldown(robber, config.getInt("rob.catch-robber.duration"));
    }
}
