package com.frahhs.robbing.feature.kidnapping;

import com.frahhs.lightlib.LightPlugin;
import com.frahhs.lightlib.feature.LightFeature;
import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.feature.kidnapping.bag.KidnappingBag;
import com.frahhs.robbing.feature.kidnapping.listener.KidnappingListener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class KidnappingFeature extends LightFeature {
    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }

    @Override
    protected void registerEvents(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(new KidnappingListener(), plugin);
    }

    @Override
    protected void registerBags(JavaPlugin javaPlugin) {
        if(!(javaPlugin instanceof Robbing))
            return;

        Robbing plugin = (Robbing) javaPlugin;

        LightPlugin.getBagManager().registerBags(new KidnappingBag());
    }

    @Override
    protected void registerItems(JavaPlugin javaPlugin) {

    }

    @Override
    protected @NotNull String getID() {
        return "kidnapping";
    }
}
