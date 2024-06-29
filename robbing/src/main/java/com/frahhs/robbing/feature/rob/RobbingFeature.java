package com.frahhs.robbing.feature.rob;

import com.frahhs.lightlib.LightPlugin;
import com.frahhs.lightlib.feature.LightFeature;
import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.feature.rob.bag.CaughtBag;
import com.frahhs.robbing.feature.rob.bag.RobbingCooldownBag;
import com.frahhs.robbing.feature.rob.bag.RobbingNowBag;
import com.frahhs.robbing.feature.rob.listener.CatchListener;
import com.frahhs.robbing.feature.rob.listener.RobListener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class RobbingFeature extends LightFeature {
    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }

    @Override
    protected void registerEvents(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(new RobListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new CatchListener(), plugin);
    }

    @Override
    protected void registerBags(JavaPlugin javaPlugin) {
        if(!(javaPlugin instanceof Robbing))
            return;

        Robbing plugin = (Robbing) javaPlugin;

        LightPlugin.getBagManager().registerBags(new RobbingNowBag());
        LightPlugin.getBagManager().registerBags(new RobbingCooldownBag());
        LightPlugin.getBagManager().registerBags(new CaughtBag());
    }

    @Override
    protected void registerItems(JavaPlugin javaPlugin) {
        
    }

    @Override
    protected @NotNull String getID() {
        return "robbing";
    }
}
