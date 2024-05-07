package com.frahhs.robbing.features.handcuffing.models;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.features.BaseModel;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Model class representing the health bar for handcuffs.
 */
public class HandcuffsBarModel extends BaseModel {
    /** Map to store active handcuffs health bars for each player. */
    public static Map<Player, HandcuffsBarModel> handcuffsActiveHealthBars = new ConcurrentHashMap<>();

    private final BossBar bar;
    private final Player player;

    private double progress;

    /**
     * Constructs a HandcuffsLifeModel instance.
     *
     * @param player The player associated with the health bar.
     */
    public HandcuffsBarModel(Player player) {
        this.player = player;

        String title = messages.getMessage("handcuffing.handcuffs_healthbar_title", false);
        BarColor color = BarColor.WHITE;
        BarStyle style = BarStyle.SOLID;
        this.bar = Robbing.getInstance().getServer().createBossBar(title, color, style);

        setProgress(1);
    }

    /**
     * Checks if a player has an active handcuffs health bar.
     *
     * @return True if the player has an active health bar, otherwise false.
     */
    public boolean haveHandcuffsBar() {
        return handcuffsActiveHealthBars.containsKey(player);
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
        this.bar.addPlayer(player);
        handcuffsActiveHealthBars.put(player, this);
    }

    /**
     * Removes the handcuffs bar from a player.
     */
    public void removeHandcuffsBar() {
        if (!haveHandcuffsBar())
            return;

        HandcuffsBarModel handcuffsBar = handcuffsActiveHealthBars.get(player);

        if (handcuffsBar != null)
            handcuffsBar.getBossBar().removePlayer(player);

        handcuffsActiveHealthBars.remove(player);
    }

    /**
     * Sets the progress of the handcuffs bar.
     *
     * @param progress The progress value.
     */
    public void setProgress(double progress) {
        this.progress = progress;
        getBossBar().setProgress(progress);
    }

    /**
     * Retrieves the progress of the health bar.
     *
     * @return The progress value.
     */
    public double getProgress() {
        return this.progress;
    }

    /**
     * Iterates the cracking of the handcuffs bar.
     *
     * @return True if the health bar is broken, false otherwise.
     */
    public boolean hit() {
        this.setProgress(getProgress() - 0.005);

        if (getProgress() <= 0.005)
            return true;

        bar.setProgress(progress);
        return false;
    }

    /**
     * Removes all handcuffs bars.
     */
    public static void removeAllHandcuffsLifeModel() {
        Set<Player> allPlayersWithHandcuffsLifeModel = handcuffsActiveHealthBars.keySet();

        for (Player cur : allPlayersWithHandcuffsLifeModel) {
            handcuffsActiveHealthBars.get(cur).getBossBar().removeAll();
            handcuffsActiveHealthBars.remove(cur);
        }
    }

    /**
     * Adds health bars to all handcuffed players.
     */
    public static void putAllHandcuffsLifeModel() {
        List<Player> allHandcuffed = new ArrayList<>();

        for (Player cur : Bukkit.getOnlinePlayers())
            if (HandcuffingModel.isHandcuffed(cur))
                allHandcuffed.add(cur);

        for (Player cur : allHandcuffed) {
            HandcuffsBarModel curHealthBar = new HandcuffsBarModel(cur);
            curHealthBar.putHandcuffsBar();
        }
    }

    /**
     * Checks if a player has an active health bar.
     *
     * @param player The player to check.
     * @return True if the player has an active health bar, otherwise false.
     */
    public static boolean haveBar(Player player) {
        return handcuffsActiveHealthBars.containsKey(player);
    }

    /**
     * Retrieves the health bar associated with a player.
     *
     * @param player The player.
     * @return The health bar associated with the player.
     */
    public static HandcuffsBarModel getBarFromPlayer(Player player) {
        return handcuffsActiveHealthBars.get(player);
    }
}
