package com.frahhs.robbing.feature.kidnapping.mcp;

import com.frahhs.lightlib.LightProvider;
import com.frahhs.robbing.feature.kidnapping.bag.KidnappingBag;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * LightProvider class for managing kidnapping data.
 */
class KidnappingProvider extends LightProvider {
    private final KidnappingBag kidnappingBag;

    /**
     * Constructs a new KidnappingProvider.
     */
    protected KidnappingProvider() {
        kidnappingBag = (KidnappingBag)bagManager.getBag("KidnappingBag");
    }

    /**
     * Checks if a player is a kidnapper.
     *
     * @param kidnapper The player to check.
     * @return True if the player is a kidnapper, otherwise false.
     */
    protected boolean isKidnapper(Player kidnapper) {
        return kidnappingBag.getData().containsKey(kidnapper.getUniqueId());
    }

    /**
     * Checks if a player is kidnapped.
     *
     * @param kidnapped The player to check.
     * @return True if the player is kidnapped, otherwise false.
     */
    protected boolean isKidnapped(Player kidnapped) {
        return kidnappingBag.getData().containsValue(kidnapped.getUniqueId());
    }

    /**
     * Saves a kidnapping event.
     *
     * @param kidnapper The kidnapper player.
     * @param kidnapped The kidnapped player.
     */
    protected void saveKidnapping(Player kidnapper, Player kidnapped) {
        kidnappingBag.getData().put(kidnapper.getUniqueId(), kidnapped.getUniqueId());
    }

    /**
     * Removes a kidnapping event.
     *
     * @param kidnapper The kidnapper player.
     */
    protected void removeKidnapping(Player kidnapper) {
        kidnappingBag.getData().remove(kidnapper.getUniqueId());
    }

    /**
     * Retrieves the Kidnapping object associated with the kidnapper player.
     *
     * @param kidnapper The kidnapper player.
     * @return The Kidnapping object, or null if not found.
     */
    protected Kidnapping getFromKidnapper(Player kidnapper) {
        if (!isKidnapper(kidnapper))
            return null;

        UUID kidnappedUUID = kidnappingBag.getData().get(kidnapper.getUniqueId());
        Player kidnapped = Bukkit.getPlayer(kidnappedUUID);

        return new Kidnapping(kidnapper, kidnapped);
    }

    /**
     * Retrieves the Kidnapping object associated with the kidnapped player.
     *
     * @param kidnapped The kidnapped player.
     * @return The Kidnapping object, or null if not found.
     */
    protected Kidnapping getFromKidnapped(Player kidnapped) {
        if (!isKidnapped(kidnapped))
            return null;

        for (UUID curKidnapperUUID : kidnappingBag.getData().keySet())
            if (kidnappingBag.getData().get(curKidnapperUUID).equals(kidnapped.getUniqueId()))
                return new Kidnapping(Bukkit.getPlayer(curKidnapperUUID), kidnapped);

        return null;
    }
}
