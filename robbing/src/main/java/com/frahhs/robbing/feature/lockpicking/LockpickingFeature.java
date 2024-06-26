package com.frahhs.robbing.feature.lockpicking;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.feature.Feature;
import com.frahhs.robbing.feature.lockpicking.listener.LockpickGUIListener;
import com.frahhs.robbing.feature.lockpicking.listener.LockpickListener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class LockpickingFeature extends Feature {
    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }

    @Override
    protected void registerEvents(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(new LockpickGUIListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new LockpickListener(), plugin);
    }

    @Override
    protected void registerBags(JavaPlugin javaPlugin) {

    }

    @Override
    protected @NotNull String getID() {
        return "lockpicking";
    }
}
