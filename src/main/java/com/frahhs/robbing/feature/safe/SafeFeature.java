package com.frahhs.robbing.feature.safe;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.feature.Feature;
import com.frahhs.robbing.feature.safe.listener.SafeListener;

public class SafeFeature extends Feature {
    private final Robbing plugin;

    public SafeFeature(Robbing plugin) {
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
        plugin.getServer().getPluginManager().registerEvents(new SafeListener(), plugin);
    }

    @Override
    protected void registerBags() {

    }

    @Override
    protected String getID() {
        return "safe";
    }
}
