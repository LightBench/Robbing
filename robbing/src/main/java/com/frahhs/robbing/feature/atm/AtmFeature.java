package com.frahhs.robbing.feature.atm;

import com.frahhs.robbing.feature.Feature;
import com.frahhs.robbing.feature.atm.listener.AtmListener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class AtmFeature extends Feature {
    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }

    @Override
    protected void registerEvents(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(new AtmListener(), plugin);
    }

    @Override
    protected void registerBags(JavaPlugin plugin) {

    }

    @Override
    protected @NotNull String getID() {
        return "atm";
    }
}
