package com.frahhs.robbing.features.handcuffing.models;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO: I don't like this implementation
public class KidnapPathModel {
    // <Kidnapper, List<Location>>
    public static Map<Player, List<Location>> paths = new HashMap<>();

    public static Location getLocation(Player kidnapper, int index) {
        return paths.get(kidnapper).get(index);
    }

    public static int getPathSize(Player kidnapper) {
        return paths.get(kidnapper).size();
    }

    public static void addLocationToPath(Player player, Player follower) {
        // Check if players are null (May happen when disconnect during kidnapping)
        if(follower == null || player == null)
            return;

        // Direction Vector, for choose where to follower have to look
        double x = player.getLocation().getX() - follower.getLocation().getX();
        double y = player.getLocation().getY() - follower.getLocation().getY();
        double z = player.getLocation().getZ() - follower.getLocation().getZ();

        Vector playerLookDirection = new Vector(x, y, z);//make a vector going from the player's location to the center point

        // Create Location to add to paths List
        Location l = new Location(player.getLocation().getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
        l.setDirection(playerLookDirection.normalize());

        // Add new location to the path, create if not exists
        if(!paths.containsKey(player)) {
            List<Location> newL = new ArrayList<>();
            newL.add(l);
            paths.put(player, newL);
        } else {
            paths.get(player).add(l);
        }

        // Make path list length maximum 20
        if(paths.get(player).size() > 20) {
            paths.get(player).remove(0);
        }
    }
}
