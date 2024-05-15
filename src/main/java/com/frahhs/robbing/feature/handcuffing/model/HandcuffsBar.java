package com.frahhs.robbing.feature.handcuffing.model;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.feature.Model;
import com.frahhs.robbing.feature.handcuffing.provider.HandcuffsBarProvider;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Model class representing the health bar for handcuffs.
 */
public class HandcuffsBar extends Model {
    private final BossBar bar;
    private final Player player;

    private HandcuffsBarProvider provider;

    /**
     * Constructs a HandcuffsBarModel instance.
     *
     * @param player The player associated with the health bar.
     */
    public HandcuffsBar(Player player) {
        this.provider = new HandcuffsBarProvider();
        this.player = player;

        String title = messages.getMessage("handcuffing.handcuffs_healthbar_title", false);
        BarColor color = BarColor.WHITE;
        BarStyle style = BarStyle.SOLID;
        this.bar = Robbing.getInstance().getServer().createBossBar(title, color, style);
    }

    /**
     * Constructs a HandcuffsBarModel instance from an existing bar.
     *
     * @param player The player associated with the health bar.
     */
    public HandcuffsBar(Player player, BossBar bar) {
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
    public void putHandcuffsBar() {
        if(!Bukkit.getOnlinePlayers().contains(player))
            return;

        this.bar.addPlayer(player);
        provider.saveHandcuffsBar(player, this.getBossBar());
    }

    /**
     * Removes the handcuffs bar from a player.
     */
    public void removeHandcuffsBar() {
        if(!Bukkit.getOnlinePlayers().contains(player))
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
    public boolean hit() {
        if(bar.getProgress() - 0.005 <= 0.005)
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

    public static boolean isHandcuffsBar(BossBar bar) {
        HandcuffsBarProvider provider = new HandcuffsBarProvider();

        return provider.isHandcuffsBar(bar);
    }

    /**
     * Retrieves the bar associated with a player.
     *
     * @param player The player.
     * @return The health bar associated with the player, null otherwise.
     */
    public static HandcuffsBar getBarFromPlayer(Player player) {
        HandcuffsBarProvider provider = new HandcuffsBarProvider();
        BossBar bar = provider.getHandcuffsBar(player).getBossBar();

        return new HandcuffsBar(player, bar);
    }

    public static List<HandcuffsBar> getAll() {
        HandcuffsBarProvider provider = new HandcuffsBarProvider();

        return provider.getAllHandcuffsBar();
    }
}
