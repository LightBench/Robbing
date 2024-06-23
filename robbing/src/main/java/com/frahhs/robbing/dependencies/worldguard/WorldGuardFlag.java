package com.frahhs.robbing.dependencies.worldguard;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class WorldGuardFlag {
    private static StateFlag ROBBING_STEAL;
    private static StateFlag ROBBING_HANDCUFFING;
    private static StateFlag ROBBING_KIDNAP;

    public static boolean checkFlag(Player player, StateFlag stateFlag) {
        // Check worldguard flag
        Location loc = player.getLocation();
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(loc));
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
        return !set.testState(localPlayer, stateFlag);
    }

    protected static void registerStealFlag() {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            // Create a flag with the name "robbing-steal", defaulting to true
            StateFlag flag = new StateFlag("robbing-steal", true);
            registry.register(flag);
            ROBBING_STEAL = flag; // only set our field if there was no error
        } catch (FlagConflictException e) {
            // Some other plugin registered a flag by the same name already.
            // You can use the existing flag, but this may cause conflicts - be sure to check type
            Flag<?> existing = registry.get("robbing-steal");
            if (existing instanceof StateFlag) {
                ROBBING_STEAL = (StateFlag) existing;
            } else {
                // types don't match - this is bad news! some other plugin conflicts with you
                // hopefully this never actually happens
                throw new RuntimeException("flag conflict!");
            }
        }
    }

    protected static void registerHandcuffingFlag() {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            // Create a flag with the name "robbing-steal", defaulting to true
            StateFlag flag = new StateFlag("robbing-handcuffing", true);
            registry.register(flag);
            ROBBING_HANDCUFFING = flag; // only set our field if there was no error
        } catch (FlagConflictException e) {
            // Some other plugin registered a flag by the same name already.
            // You can use the existing flag, but this may cause conflicts - be sure to check type
            Flag<?> existing = registry.get("robbing-handcuffing");
            if (existing instanceof StateFlag) {
                ROBBING_HANDCUFFING = (StateFlag) existing;
            } else {
                // types don't match - this is bad news! some other plugin conflicts with you
                // hopefully this never actually happens
                throw new RuntimeException("flag conflict!");
            }
        }
    }

    protected static void registerKidnapFlag() {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            // Create a flag with the name "robbing-steal", defaulting to true
            StateFlag flag = new StateFlag("robbing-kidnap", true);
            registry.register(flag);
            ROBBING_KIDNAP = flag; // only set our field if there was no error
        } catch (FlagConflictException e) {
            // Some other plugin registered a flag by the same name already.
            // You can use the existing flag, but this may cause conflicts - be sure to check type
            Flag<?> existing = registry.get("robbing-kidnap");
            if (existing instanceof StateFlag) {
                ROBBING_KIDNAP = (StateFlag) existing;
            } else {
                // types don't match - this is bad news! some other plugin conflicts with you
                // hopefully this never actually happens
                throw new RuntimeException("flag conflict!");
            }
        }
    }

    public static boolean checkStealFlag(Player player) {
        return checkFlag(player, WorldGuardFlag.ROBBING_STEAL);
    }

    public static boolean checkHandcuffingFlag(Player player) {
        return checkFlag(player, WorldGuardFlag.ROBBING_HANDCUFFING);
    }

    public static boolean checkKidnapFlag(Player player) {
        return checkFlag(player, WorldGuardFlag.ROBBING_KIDNAP);
    }
}