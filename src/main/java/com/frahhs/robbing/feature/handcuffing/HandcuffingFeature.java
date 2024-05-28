package com.frahhs.robbing.feature.handcuffing;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.feature.Feature;
import com.frahhs.robbing.feature.handcuffing.bag.HandcuffingCooldownBag;
import com.frahhs.robbing.feature.handcuffing.bag.HandcuffsBarBag;
import com.frahhs.robbing.feature.handcuffing.listener.HandcuffedListener;
import com.frahhs.robbing.feature.handcuffing.listener.HandcuffingListener;
import com.frahhs.robbing.feature.handcuffing.listener.HitHandcuffsListener;
import com.frahhs.robbing.feature.handcuffing.mcp.Handcuffing;
import com.frahhs.robbing.feature.handcuffing.mcp.HandcuffsBar;
import com.frahhs.robbing.feature.handcuffing.mcp.HandcuffsBarController;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HandcuffingFeature extends Feature {
    private final Robbing plugin;

    public HandcuffingFeature(Robbing plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void onEnable() {
        // Handle Handcuffs bars
        HandcuffsBarController handcuffsBarController = new HandcuffsBarController();
        List<Player> allHandcuffed = new ArrayList<>();

        for(Player cur : Bukkit.getOnlinePlayers())
            if (Handcuffing.isHandcuffed(cur))
                allHandcuffed.add(cur);

        for(Player cur : allHandcuffed)
            handcuffsBarController.put(cur);
    }

    @Override
    protected void onDisable() {
        // Handle Handcuffs bars
        HandcuffsBarController handcuffsBarController = new HandcuffsBarController();

        for(HandcuffsBar curBar : HandcuffsBar.getAll())
            if(curBar != null)
                for (Player curPlayer : curBar.getBossBar().getPlayers())
                    handcuffsBarController.remove(curPlayer);
    }

    @Override
    protected void registerEvents() {
        plugin.getServer().getPluginManager().registerEvents(new HandcuffingListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new HandcuffedListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new HitHandcuffsListener(), plugin);
    }

    @Override
    protected void registerBags() {
        plugin.getBagManager().registerBags(new HandcuffingCooldownBag());
        plugin.getBagManager().registerBags(new HandcuffsBarBag());
    }

    @Override
    protected String getID() {
        return "handcuffing";
    }
}
