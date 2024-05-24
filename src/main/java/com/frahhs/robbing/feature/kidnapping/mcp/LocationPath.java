package com.frahhs.robbing.feature.kidnapping.mcp;

import com.frahhs.robbing.RobbingObject;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Model class representing the path for kidnapping.
 */
public class LocationPath extends RobbingObject {
    protected static Map<Player, List<Location>> paths = new HashMap<>();

    /**
     * Retrieves a location from the path based on the index.
     *
     * @param kidnapper The kidnapper player.
     * @param index     The index of the location in the path.
     * @return The location from the path.
     */
    private Location getLocation(Player kidnapper, int index) {
        return paths.get(kidnapper).get(index);
    }

    /**
     * Retrieves the size of the path for a kidnapper.
     *
     * @param kidnapper The kidnapper player.
     * @return The size of the path.
     */
    private int getPathSize(Player kidnapper) {
        return paths.get(kidnapper).size();
    }

    /**
     * Adds a location to the path.
     *
     * @param player   The player.
     * @param follower The follower player.
     */
    public void addLocationToPath(Player player, Player follower) {
        if(follower == null || player == null)
            return;

        double x = player.getLocation().getX() - follower.getLocation().getX();
        double y = player.getLocation().getY() - follower.getLocation().getY();
        double z = player.getLocation().getZ() - follower.getLocation().getZ();

        Vector playerLookDirection = new Vector(x, y, z);
        Location l = new Location(player.getLocation().getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
        l.setDirection(playerLookDirection.normalize());

        if(!paths.containsKey(player)) {
            List<Location> newL = new ArrayList<>();
            newL.add(l);
            paths.put(player, newL);
        } else {
            paths.get(player).add(l);
        }

        if(paths.get(player).size() > 20) {
            paths.get(player).remove(0);
        }
    }

    public void update(Player kidnapper, Player kidnapped) {
        if(getPathSize(kidnapper) > 10) {
            if(kidnapper.getLocation().distance(kidnapped.getLocation()) > 2)
                kidnapped.teleport(getLocation(kidnapper, 10));
        }
    }

    protected void removePlayerPath(Player player) {
        paths.remove(player);
    }
}
