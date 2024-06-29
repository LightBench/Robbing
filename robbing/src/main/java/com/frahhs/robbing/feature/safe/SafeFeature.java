package com.frahhs.robbing.feature.safe;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.feature.Feature;
import com.frahhs.robbing.feature.safe.bag.SafeInventoryBag;
import com.frahhs.robbing.feature.safe.listener.SafeListener;
import com.frahhs.robbing.feature.safe.listener.SafeUnlockGUIListener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class SafeFeature extends Feature {
    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }

    @Override
    protected void registerEvents(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(new SafeListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new SafeUnlockGUIListener(), plugin);
    }

    @Override
    protected void registerBags(JavaPlugin javaPlugin) {
        if(!(javaPlugin instanceof Robbing))
            return;

        Robbing plugin = (Robbing) javaPlugin;

        plugin.getBagManager().registerBags(new SafeInventoryBag());
    }

    @Override
    protected @NotNull String getID() {
        return "safe";
    }
}
