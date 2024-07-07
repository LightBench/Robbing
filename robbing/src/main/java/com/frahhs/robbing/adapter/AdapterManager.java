package com.frahhs.robbing.adapter;

import com.frahhs.robbing.Robbing;
import org.bukkit.Bukkit;

public class AdapterManager {
    public static void adapt() {
        Bukkit.getPluginManager().registerEvents(new ItemDisplayAdapter(), Robbing.getInstance());
        ItemDisplayAdapter.adapt();
        Bukkit.getPluginManager().registerEvents(new SafePinAdapter(), Robbing.getInstance());
        SafePinAdapter.adapt();
    }
}
