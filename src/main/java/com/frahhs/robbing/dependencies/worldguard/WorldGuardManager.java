package com.frahhs.robbing.dependencies.worldguard;

import com.frahhs.robbing.Robbing;

public class WorldGuardManager extends WorldGuardFlag {
    public static boolean worldguardInstalled() {
        return Robbing.getInstance().getServer().getPluginManager().getPlugin("WorldGuard") != null;
    }
}