package com.frahhs.robbing.feature.handcuffing.mcp;

import com.frahhs.lightlib.feature.LightModel;
import com.frahhs.robbing.Robbing;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * LightModel class representing the health bar for handcuffs.
 */
public class HandcuffsBar extends LightModel {
    private final BossBar bar;
    private final Player player;

    private HandcuffsBarProvider provider;

    /**
     * Constructs a HandcuffsBar instance.
     *
     * @param player The player associated with the handcuffs bar.
     */
    protected HandcuffsBar(Player player) {
        this.provider = new HandcuffsBarProvider();
        this.player = player;

        String title = messages.getMessage("handcuffing.handcuffs_healthbar_title", false);
        BarColor color = BarColor.WHITE;
        BarStyle style = BarStyle.SOLID;
        this.bar = Robbing.getInstance().getServer().createBossBar(title, color, style);
    }

    /**
     * Constructs a HandcuffsBar instance from an existing bar.
     *
     * @param player The player associated with the handcuffs bar.
     * @param bar    The existing boss bar.
     */
    protected HandcuffsBar(Player player, BossBar bar) {
        this.provider = new HandcuffsBarProvider();
        this.player = player;
        this.bar = bar;
    }

    /**
     * Checks if a player has an active handcuffs health bar.
     *
     * @return True if the player has an active health bar, otherwise false.
     */
    public boolean haveHandcuffsBar() {
        return provider.haveHandcuffsBar(player);
    }

    /**
     * Retrieves the boss bar associated with the health bar.
     *
     * @return The boss bar.
     */
    public BossBar getBossBar() {
        return this.bar;
    }

    /**
     * Adds the handcuffs bar to a player.
     */
    protected void putHandcuffsBar() {
        if (!Bukkit.getOnlinePlayers().contains(player))
            return;

        this.bar.addPlayer(player);
        provider.saveHandcuffsBar(player, this.bar);
    }

    /**
     * Removes the handcuffs bar from a player.
     */
    protected void removeHandcuffsBar() {
        if (!Bukkit.getOnlinePlayers().contains(player))
            return;

        if (bar != null)
            bar.removePlayer(player);
        provider.removeHandcuffsBar(player);
    }

    /**
     * Iterates the cracking of the handcuffs bar.
     *
     * @return True if the health bar is broken, false otherwise.
     */
    protected boolean hit() {
        if (bar.getProgress() - 0.005 <= 0.005)
            return true;

        bar.setProgress(bar.getProgress() - 0.005);
        return false;
    }

    /**
     * Checks if a player has an active bar.
     *
     * @param player The player to check.
     * @return True if the player has an active health bar, false otherwise.
     */
    public static boolean haveBar(Player player) {
        HandcuffsBarProvider provider = new HandcuffsBarProvider();
        return provider.haveHandcuffsBar(player);
    }

    /**
     * Checks if a boss bar is a handcuffs bar.
     *
     * @param bar The boss bar to check.
     * @return True if the boss bar is a handcuffs bar, false otherwise.
     */
    public static boolean isHandcuffsBar(BossBar bar) {
        HandcuffsBarProvider provider = new HandcuffsBarProvider();
        return provider.isHandcuffsBar(bar);
    }

    /**
     * Retrieves the handcuffs bar associated with a player.
     *
     * @param player The player.
     * @return The handcuffs bar associated with the player, or null if not found.
     */
    public static HandcuffsBar getBarFromPlayer(Player player) {
        if(!haveBar(player))
            return null;

        HandcuffsBarProvider provider = new HandcuffsBarProvider();
        BossBar bar = provider.getHandcuffsBar(player).getBossBar();
        return new HandcuffsBar(player, bar);
    }

    /**
     * Retrieves a list of all active handcuffs bars.
     *
     * @return A list of handcuffs bars.
     */
    public static List<HandcuffsBar> getAll() {
        HandcuffsBarProvider provider = new HandcuffsBarProvider();
        return provider.getAllHandcuffsBar();
    }
}
