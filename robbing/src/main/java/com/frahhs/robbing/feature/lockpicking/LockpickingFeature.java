package com.frahhs.robbing.feature.lockpicking;

import com.frahhs.lightlib.LightPlugin;
import com.frahhs.lightlib.feature.LightFeature;
import com.frahhs.robbing.feature.lockpicking.item.*;
import com.frahhs.robbing.feature.lockpicking.listener.LockpickGUIListener;
import com.frahhs.robbing.feature.lockpicking.listener.LockpickListener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class LockpickingFeature extends LightFeature {
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
    protected void registerItems(JavaPlugin javaPlugin) {
        LightPlugin.getItemsManager().registerItems(new Lockpick(), javaPlugin);
        LightPlugin.getItemsManager().registerItems(new PanelNumber0(), javaPlugin);
        LightPlugin.getItemsManager().registerItems(new PanelNumber1(), javaPlugin);
        LightPlugin.getItemsManager().registerItems(new PanelNumber2(), javaPlugin);
        LightPlugin.getItemsManager().registerItems(new PanelNumber3(), javaPlugin);
        LightPlugin.getItemsManager().registerItems(new PanelNumber4(), javaPlugin);
        LightPlugin.getItemsManager().registerItems(new PanelNumber5(), javaPlugin);
        LightPlugin.getItemsManager().registerItems(new PanelNumber6(), javaPlugin);
        LightPlugin.getItemsManager().registerItems(new PanelNumber7(), javaPlugin);
        LightPlugin.getItemsManager().registerItems(new PanelNumber8(), javaPlugin);
        LightPlugin.getItemsManager().registerItems(new PanelNumber9(), javaPlugin);
        LightPlugin.getItemsManager().registerItems(new PanelNumberCancel(), javaPlugin);
        LightPlugin.getItemsManager().registerItems(new PanelNumberCheck(), javaPlugin);
        LightPlugin.getItemsManager().registerItems(new Cylinder(), javaPlugin);
        LightPlugin.getItemsManager().registerItems(new CylinderWrong(), javaPlugin);
        LightPlugin.getItemsManager().registerItems(new CylinderCorrect(), javaPlugin);
    }

    @Override
    protected @NotNull String getID() {
        return "lockpicking";
    }
}
