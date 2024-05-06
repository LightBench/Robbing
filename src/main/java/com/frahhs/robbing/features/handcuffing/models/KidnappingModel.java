package com.frahhs.robbing.features.handcuffing.models;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class KidnappingModel {
    private final Player kidnapped;
    private final Player kidnapper;

    // TODO: Implement in database
    public static Map<Player, Player> kidnapping_list = new HashMap<>();

    public KidnappingModel(Player kidnapper, Player kidnapped) {
        this.kidnapper = kidnapper;
        this.kidnapped = kidnapped;
    }

    public Player getKidnapped(){
        return kidnapped;
    }

    public Player getKidnapper() {
        return kidnapper;
    }

    public void kidnap() {
        KidnappingModel.kidnapping_list.put(kidnapper, kidnapped);
    }

    public void free() {
        KidnappingModel.kidnapping_list.remove(kidnapper);
    }

    public static boolean isKidnapped(Player kidnapped) {
        return kidnapping_list.containsValue(kidnapped);
    }

    public static boolean isKidnapper(Player kidnapper) {
        return kidnapping_list.containsKey(kidnapper);
    }

    public static KidnappingModel getFromKidnapper(Player kidnapper) {
        if(!isKidnapper(kidnapper))
            return null;

        return new KidnappingModel(kidnapper, kidnapping_list.get(kidnapper));
    }

    public static KidnappingModel getFromKidnapped(Player kidnapped) {
        if(!isKidnapped(kidnapped))
            return null;

        for(Player curKidnapper : kidnapping_list.keySet())
            if(kidnapping_list.get(curKidnapper) == kidnapped)
                return new KidnappingModel(curKidnapper, kidnapped);

        return null;
    }
}
