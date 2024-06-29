package com.frahhs.robbing.feature.safe;

import com.frahhs.lightlib.LightPlugin;
import com.frahhs.lightlib.feature.LightFeature;
import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.feature.safe.bag.SafeInventoryBag;
import com.frahhs.robbing.feature.safe.database.SafeDatabase;
import com.frahhs.robbing.feature.safe.listener.SafeListener;
import com.frahhs.robbing.feature.safe.listener.SafeUnlockGUIListener;
import com.frahhs.robbing.feature.safe.item.Safe;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class SafeFeature extends LightFeature {
    @Override
    protected void onEnable() {
        SafeDatabase.createSafeInventoryTable();
        SafeDatabase.createSafeLockedTable();
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

        LightPlugin.getBagManager().registerBags(new SafeInventoryBag());
    }

    @Override
    protected void registerItems(JavaPlugin javaPlugin) {
        LightPlugin.getItemsManager().registerItems(new Safe(), javaPlugin);
    }

    @Override
    protected @NotNull String getID() {
        return "safe";
    }
}
