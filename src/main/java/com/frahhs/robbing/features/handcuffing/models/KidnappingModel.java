package com.frahhs.robbing.features.handcuffing.models;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Model class representing a kidnapping event.
 */
public class KidnappingModel {
    private final Player kidnapped;
    private final Player kidnapper;

    // TODO: Implement in database
    public static Map<Player, Player> kidnappingList = new HashMap<>();

    /**
     * Constructs a KidnappingModel object.
     *
     * @param kidnapper The player who is the kidnapper.
     * @param kidnapped The player who is kidnapped.
     */
    public KidnappingModel(Player kidnapper, Player kidnapped) {
        this.kidnapper = kidnapper;
        this.kidnapped = kidnapped;
    }

    /**
     * Retrieves the kidnapped player.
     *
     * @return The kidnapped player.
     */
    public Player getKidnapped(){
        return kidnapped;
    }

    /**
     * Retrieves the kidnapper player.
     *
     * @return The kidnapper player.
     */
    public Player getKidnapper() {
        return kidnapper;
    }

    /**
     * Records the kidnapping event.
     */
    public void kidnap() {
        kidnappingList.put(kidnapper, kidnapped);
    }

    /**
     * Frees the kidnapped player.
     */
    public void free() {
        kidnappingList.remove(kidnapper);
    }

    /**
     * Checks if a player is kidnapped.
     *
     * @param kidnapped The player to check if kidnapped.
     * @return True if the player is kidnapped, otherwise false.
     */
    public static boolean isKidnapped(Player kidnapped) {
        return kidnappingList.containsValue(kidnapped);
    }

    /**
     * Checks if a player is a kidnapper.
     *
     * @param kidnapper The player to check if kidnapper.
     * @return True if the player is a kidnapper, otherwise false.
     */
    public static boolean isKidnapper(Player kidnapper) {
        return kidnappingList.containsKey(kidnapper);
    }

    /**
     * Retrieves the KidnappingModel object from the kidnapper player.
     *
     * @param kidnapper The kidnapper player.
     * @return The KidnappingModel object.
     */
    public static KidnappingModel getFromKidnapper(Player kidnapper) {
        if(!isKidnapper(kidnapper))
            return null;

        return new KidnappingModel(kidnapper, kidnappingList.get(kidnapper));
    }

    /**
     * Retrieves the KidnappingModel object from the kidnapped player.
     *
     * @param kidnapped The kidnapped player.
     * @return The KidnappingModel object.
     */
    public static KidnappingModel getFromKidnapped(Player kidnapped) {
        if(!isKidnapped(kidnapped))
            return null;

        for(Player curKidnapper : kidnappingList.keySet())
            if(kidnappingList.get(curKidnapper) == kidnapped)
                return new KidnappingModel(curKidnapper, kidnapped);

        return null;
    }
}
