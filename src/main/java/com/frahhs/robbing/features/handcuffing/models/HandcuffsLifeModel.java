package com.frahhs.robbing.features.handcuffing.models;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.managers.MessagesManager;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.*;

public class HandcuffsLifeModel {
    private final MessagesManager messagesManager = Robbing.getInstance().getMessagesManager();

    public static Map<Player, HandcuffsLifeModel> handcuffsActiveHealthBars = new HashMap<>();

    private final BossBar bar;
    private double progress;

    // TODO: Make BossBar style customizable
    public HandcuffsLifeModel() {
        String title = messagesManager.getMessage("handcuffing.handcuffs_healthbar_title", false);
        BarColor color = BarColor.WHITE;
        BarStyle style = BarStyle.SOLID;
        this.bar = Robbing.getInstance().getServer().createBossBar(title, color, style);
        progress = 1;
        this.bar.setProgress(progress);
    }

    public void putHandcuffsLifeModel(Player handcuffed) {
        // Put HandcuffsLifeModel to a player
        this.bar.addPlayer(handcuffed);
        handcuffsActiveHealthBars.put(handcuffed, this);
    }

    public void removeHandcuffsLifeModel(Player handcuffed) {
        // Remove HandcuffsLifeModel to a player
        HandcuffsLifeModel handcuffsLifeModel = getBarFromPlayer(handcuffed);

        if(handcuffsLifeModel != null)
            handcuffsLifeModel.getBar().removePlayer(handcuffed);

        handcuffsActiveHealthBars.remove(handcuffed);
    }

    public boolean crack() {
        // Crack HandcuffsLifeModel iteration, return true when HandcuffsLifeModel is broken
        this.setProgress(getProgress() - 0.005);

        if(getProgress() <= 0.005)
            return true;

        bar.setProgress(progress);
        return false;
    }

    public void setProgress(double progress) {
        this.progress = progress;
        bar.setProgress(progress);
    }

    public static void removeAllHandcuffsLifeModel() {
        Set<Player> allPlayersWithHandcuffsLifeModel = handcuffsActiveHealthBars.keySet();

        for(Player cur : allPlayersWithHandcuffsLifeModel) {
            handcuffsActiveHealthBars.get(cur).getBar().removeAll();
            handcuffsActiveHealthBars.remove(cur);
        }
    }

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

    public double getProgress() { return this.progress; }

    public static HandcuffsLifeModel getBarFromPlayer(Player p) {
        return handcuffsActiveHealthBars.get(p);
    }

    public BossBar getBar() { return this.bar; }
}
