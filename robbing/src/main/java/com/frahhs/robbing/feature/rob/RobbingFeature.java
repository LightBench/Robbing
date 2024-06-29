package com.frahhs.robbing.feature.rob;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.feature.Feature;
import com.frahhs.robbing.feature.rob.bag.CaughtBag;
import com.frahhs.robbing.feature.rob.bag.RobbingCooldownBag;
import com.frahhs.robbing.feature.rob.bag.RobbingNowBag;
import com.frahhs.robbing.feature.rob.listener.CatchListener;
import com.frahhs.robbing.feature.rob.listener.RobListener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class RobbingFeature extends Feature {
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

        plugin.getBagManager().registerBags(new RobbingNowBag());
        plugin.getBagManager().registerBags(new RobbingCooldownBag());
        plugin.getBagManager().registerBags(new CaughtBag());
    }

    @Override
    protected @NotNull String getID() {
        return "robbing";
    }
}
