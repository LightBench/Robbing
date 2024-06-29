package com.frahhs.robbing.feature.handcuffing;

import com.frahhs.lightlib.LightPlugin;
import com.frahhs.lightlib.feature.LightFeature;
import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.feature.handcuffing.bag.HandcuffingCooldownBag;
import com.frahhs.robbing.feature.handcuffing.bag.HandcuffsBarBag;
import com.frahhs.robbing.feature.handcuffing.bag.JustHandcuffBag;
import com.frahhs.robbing.feature.handcuffing.database.HandcuffingDatabase;
import com.frahhs.robbing.feature.handcuffing.listener.HandcuffedListener;
import com.frahhs.robbing.feature.handcuffing.listener.HandcuffingListener;
import com.frahhs.robbing.feature.handcuffing.listener.HitHandcuffsListener;
import com.frahhs.robbing.feature.handcuffing.mcp.Handcuffing;
import com.frahhs.robbing.feature.handcuffing.mcp.HandcuffsBar;
import com.frahhs.robbing.feature.handcuffing.mcp.HandcuffsBarController;
import com.frahhs.robbing.feature.handcuffing.item.Handcuffs;
import com.frahhs.robbing.feature.handcuffing.item.HandcuffsKey;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HandcuffingFeature extends LightFeature {
    @Override
    protected void onEnable() {
        HandcuffingDatabase.createHandcuffingTable();

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
    protected void registerEvents(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(new HandcuffingListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new HandcuffedListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new HitHandcuffsListener(), plugin);
    }

    @Override
    protected void registerBags(JavaPlugin javaPlugin) {
        if(!(javaPlugin instanceof Robbing))
            return;

        LightPlugin.getBagManager().registerBags(new HandcuffingCooldownBag());
        LightPlugin.getBagManager().registerBags(new HandcuffsBarBag());
        LightPlugin.getBagManager().registerBags(new JustHandcuffBag());
    }

    @Override
    protected void registerItems(JavaPlugin javaPlugin) {
        LightPlugin.getItemsManager().registerItems(new Handcuffs(), javaPlugin);
        LightPlugin.getItemsManager().registerItems(new HandcuffsKey(), javaPlugin);
    }

    @Override
    protected @NotNull String getID() {
        return "handcuffing";
    }
}
