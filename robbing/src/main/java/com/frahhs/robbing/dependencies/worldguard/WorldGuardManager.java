package com.frahhs.robbing.dependencies.worldguard;

public class WorldGuardManager {
    public static void registerFlags() {
        WorldGuardFlag.registerStealFlag();
        WorldGuardFlag.registerHandcuffingFlag();
        WorldGuardFlag.registerKidnapFlag();
    }
}