package com.frahhs.robbing.features.handcuffing.models;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.features.BaseModel;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Model class representing the health bar for handcuffs.
 */
public class HandcuffsLifeModel extends BaseModel {
    public static Map<Player, HandcuffsLifeModel> handcuffsActiveHealthBars = new HashMap<>();

    private final BossBar bar;
    private double progress;

    /**
     * Constructs a HandcuffsLifeModel instance.
     */
    public HandcuffsLifeModel() {
        String title = messages.getMessage("handcuffing.handcuffs_healthbar_title", false);
        BarColor color = BarColor.WHITE;
        BarStyle style = BarStyle.SOLID;
        this.bar = Robbing.getInstance().getServer().createBossBar(title, color, style);
        progress = 1;
        this.bar.setProgress(progress);
    }

    /**
     * Adds the health bar to a player.
     *
     * @param handcuffed The player who has the health bar.
     */
    public void putHandcuffsLifeModel(Player handcuffed) {
        this.bar.addPlayer(handcuffed);
        handcuffsActiveHealthBars.put(handcuffed, this);
    }

    /**
     * Removes the health bar from a player.
     *
     * @param handcuffed The player whose health bar is removed.
     */
    public void removeHandcuffsLifeModel(Player handcuffed) {
        HandcuffsLifeModel handcuffsLifeModel = getBarFromPlayer(handcuffed);

        if(handcuffsLifeModel != null)
            handcuffsLifeModel.getBar().removePlayer(handcuffed);

        handcuffsActiveHealthBars.remove(handcuffed);
    }

    /**
     * Iterates the cracking of the health bar.
     *
     * @return True if the health bar is broken, false otherwise.
     */
    public boolean crack() {
        this.setProgress(getProgress() - 0.005);

        if(getProgress() <= 0.005)
            return true;

        bar.setProgress(progress);
        return false;
    }

    /**
     * Sets the progress of the health bar.
     *
     * @param progress The progress value.
     */
    public void setProgress(double progress) {
        this.progress = progress;
        bar.setProgress(progress);
    }

    /**
     * Removes all health bars.
     */
    public static void removeAllHandcuffsLifeModel() {
        Set<Player> allPlayersWithHandcuffsLifeModel = handcuffsActiveHealthBars.keySet();

        for(Player cur : allPlayersWithHandcuffsLifeModel) {
            handcuffsActiveHealthBars.get(cur).getBar().removeAll();
            handcuffsActiveHealthBars.remove(cur);
        }
    }

    /**
     * Adds health bars to all handcuffed players.
     */
    public static void putAllHandcuffsLifeModel() {
        List<Player> allHandcuffed = new ArrayList<>();

        for(Player cur : Bukkit.getOnlinePlayers())
            if(HandcuffingModel.isHandcuffed(cur))
                allHandcuffed.add(cur);

        for(Player cur : allHandcuffed) {
            HandcuffsLifeModel curHealthBar = new HandcuffsLifeModel();
            curHealthBar.putHandcuffsLifeModel(cur);
        }
    }

    /**
     * Retrieves the progress of the health bar.
     *
     * @return The progress value.
     */
    public double getProgress() { return this.progress; }

    /**
     * Retrieves the health bar associated with a player.
     *
     * @param p The player.
     * @return The health bar associated with the player.
     */
    public static HandcuffsLifeModel getBarFromPlayer(Player p) {
        return handcuffsActiveHealthBars.get(p);
    }

    /**
     * Retrieves the boss bar.
     *
     * @return The boss bar.
     */
    public BossBar getBar() { return this.bar; }
}
