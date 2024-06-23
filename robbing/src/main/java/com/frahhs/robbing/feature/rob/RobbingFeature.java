package com.frahhs.robbing.feature.rob;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.feature.Feature;
import com.frahhs.robbing.feature.rob.bag.CaughtBag;
import com.frahhs.robbing.feature.rob.bag.RobbingCooldownBag;
import com.frahhs.robbing.feature.rob.bag.RobbingNowBag;
import com.frahhs.robbing.feature.rob.listener.CatchListener;
import com.frahhs.robbing.feature.rob.listener.RobListener;
import org.jetbrains.annotations.NotNull;

public class RobbingFeature extends Feature {
    private final Robbing plugin;

    public RobbingFeature(Robbing plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }

    @Override
    protected void registerEvents() {
        plugin.getServer().getPluginManager().registerEvents(new RobListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new CatchListener(), plugin);
    }

    @Override
    protected void registerBags() {
        plugin.getBagManager().registerBags(new RobbingNowBag());
        plugin.getBagManager().registerBags(new RobbingCooldownBag());
        plugin.getBagManager().registerBags(new CaughtBag());
    }

    @Override
    protected @NotNull String getID() {
        return "robbing";
    }
}
