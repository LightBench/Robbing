package com.frahhs.robbing.feature.kidnapping;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.feature.Feature;
import com.frahhs.robbing.feature.kidnapping.bag.KidnappingBag;
import com.frahhs.robbing.feature.kidnapping.listener.KidnappingListener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class KidnappingFeature extends Feature {
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

        plugin.getBagManager().registerBags(new KidnappingBag());
    }

    @Override
    protected @NotNull String getID() {
        return "kidnapping";
    }
}
