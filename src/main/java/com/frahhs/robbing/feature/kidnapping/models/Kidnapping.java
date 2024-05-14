package com.frahhs.robbing.feature.kidnapping.models;

import com.frahhs.robbing.feature.BaseModel;
import com.frahhs.robbing.feature.kidnapping.provider.KidnappingProvider;
import org.bukkit.entity.Player;

/**
 * Model class representing a kidnapping event.
 */
public class Kidnapping extends BaseModel {
    private final Player kidnapped;
    private final Player kidnapper;

    private final KidnappingProvider provider;

    /**
     * Constructs a Kidnapping object.
     *
     * @param kidnapper The player who is the kidnapper.
     * @param kidnapped The player who is kidnapped.
     */
    public Kidnapping(Player kidnapper, Player kidnapped) {
        this.kidnapper = kidnapper;
        this.kidnapped = kidnapped;

        this.provider = new KidnappingProvider();
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
        provider.saveKidnapping(kidnapper, kidnapped);
    }

    /**
     * Frees the kidnapped player.
     */
    public void free() {
        provider.removeKidnapping(kidnapper);
    }

    /**
     * Checks if a player is kidnapped.
     *
     * @param kidnapped The player to check if kidnapped.
     * @return True if the player is kidnapped, otherwise false.
     */
    public static boolean isKidnapped(Player kidnapped) {
        KidnappingProvider provider = new KidnappingProvider();
        return provider.isKidnapped(kidnapped);
    }

    /**
     * Checks if a player is a kidnapper.
     *
     * @param kidnapper The player to check if kidnapper.
     * @return True if the player is a kidnapper, otherwise false.
     */
    public static boolean isKidnapper(Player kidnapper) {
        KidnappingProvider provider = new KidnappingProvider();
        return provider.isKidnapper(kidnapper);
    }

    /**
     * Retrieves the Kidnapping object from the kidnapper player.
     *
     * @param kidnapper The kidnapper player.
     * @return The Kidnapping object.
     */
    public static Kidnapping getFromKidnapper(Player kidnapper) {
        KidnappingProvider provider = new KidnappingProvider();
        return provider.getFromKidnapper(kidnapper);
    }

    /**
     * Retrieves the Kidnapping object from the kidnapped player.
     *
     * @param kidnapped The kidnapped player.
     * @return The Kidnapping object.
     */
    public static Kidnapping getFromKidnapped(Player kidnapped) {
        KidnappingProvider provider = new KidnappingProvider();
        return provider.getFromKidnapped(kidnapped);
    }
}
