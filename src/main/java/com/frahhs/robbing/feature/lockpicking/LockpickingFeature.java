package com.frahhs.robbing.feature.lockpicking;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.feature.Feature;
import com.frahhs.robbing.feature.lockpicking.listener.LockpickGUIListener;
import com.frahhs.robbing.feature.lockpicking.listener.LockpickListener;
import org.jetbrains.annotations.NotNull;

public class LockpickingFeature extends Feature {
    private final Robbing plugin;

    public LockpickingFeature(Robbing plugin) {
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
        plugin.getServer().getPluginManager().registerEvents(new LockpickGUIListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new LockpickListener(), plugin);
    }

    @Override
    protected void registerBags() {

    }

    @Override
    protected @NotNull String getID() {
        return "lockpicking";
    }
}
