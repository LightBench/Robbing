package com.frahhs.robbing.feature.kidnapping.mcp;

import com.frahhs.robbing.feature.Controller;
import com.frahhs.robbing.feature.kidnapping.events.ToggleKidnapEvent;
import com.frahhs.robbing.feature.kidnapping.mcp.LocationPath;
import com.frahhs.robbing.feature.kidnapping.mcp.Kidnapping;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Controller class for managing kidnapping operations.
 */
public class KidnappingController extends Controller {
    /**
     * Initiates a kidnapping.
     *
     * @param kidnapper The player who is the kidnapper.
     * @param kidnapped The player who is kidnapped.
     */
    public void kidnap(Player kidnapper, Player kidnapped) {
        ToggleKidnapEvent toggleKidnapEvent = new ToggleKidnapEvent(kidnapper, kidnapped, true);
        Bukkit.getPluginManager().callEvent(toggleKidnapEvent);

        Kidnapping kidnapping = new Kidnapping(kidnapper, kidnapped);

        if (!toggleKidnapEvent.isCancelled()) {
            kidnapping.setKidnap();
        }
    }

    /**
     * Frees a kidnapped player.
     *
     * @param kidnapped The player who is kidnapped.
     */
    public void free(Player kidnapped) {
        LocationPath locationPath = new LocationPath();
        if (Kidnapping.isKidnapped(kidnapped)) {
            ToggleKidnapEvent toggleKidnapEvent = new ToggleKidnapEvent(Kidnapping.getFromKidnapped(kidnapped).getKidnapper(), kidnapped, true);
            Bukkit.getPluginManager().callEvent(toggleKidnapEvent);

            if (!toggleKidnapEvent.isCancelled()) {
                Kidnapping kidnapping = Kidnapping.getFromKidnapped(kidnapped);
                kidnapping.removeKidnap();
                locationPath.removePlayerPath(kidnapping.getKidnapper());
            }
        }
    }

    /**
     * Retrieves the kidnapper of a kidnapped player.
     *
     * @param kidnapped The player who is kidnapped.
     * @return The player who is the kidnapper, or null if not found.
     */
    public Player getKidnapper(Player kidnapped) {
        if (Kidnapping.isKidnapped(kidnapped)) {
            return Kidnapping.getFromKidnapped(kidnapped).getKidnapper();
        } else {
            return null;
        }
    }

    /**
     * Retrieves the kidnapped player of a kidnapper.
     *
     * @param kidnapper The player who is the kidnapper.
     * @return The player who is kidnapped, or null if not found.
     */
    public Player getKidnapped(Player kidnapper) {
        if (Kidnapping.isKidnapper(kidnapper)) {
            return Kidnapping.getFromKidnapper(kidnapper).getKidnapped();
        } else {
            return null;
        }
    }
}
