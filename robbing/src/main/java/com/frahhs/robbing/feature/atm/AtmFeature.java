package com.frahhs.robbing.feature.atm;

import com.frahhs.lightlib.LightPlugin;
import com.frahhs.lightlib.feature.LightFeature;
import com.frahhs.robbing.feature.atm.listener.AtmListener;
import com.frahhs.robbing.feature.atm.item.ATM;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class AtmFeature extends LightFeature {
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
    protected void registerItems(JavaPlugin javaPlugin) {
        LightPlugin.getItemsManager().registerItems(new ATM(), javaPlugin);
    }

    @Override
    protected @NotNull String getID() {
        return "atm";
    }
}
