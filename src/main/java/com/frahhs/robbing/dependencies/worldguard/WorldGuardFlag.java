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
    public static StateFlag ROBBING_STEAL;

    public static void registerStealFlag() {
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
                throw new RuntimeException("Steal flag conflict!");
            }
        }
    }

    public static boolean checkStealFlag(Player p) {
        // Check worldguard flag
        Location loc = p.getLocation();
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(loc));
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(p);
        return !set.testState(localPlayer, WorldGuardFlag.ROBBING_STEAL);
    }
}