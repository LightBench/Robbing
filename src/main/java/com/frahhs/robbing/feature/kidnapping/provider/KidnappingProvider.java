package com.frahhs.robbing.feature.kidnapping.provider;

import com.frahhs.robbing.feature.BaseProvider;
import com.frahhs.robbing.feature.kidnapping.bag.KidnappingBag;
import com.frahhs.robbing.feature.kidnapping.models.Kidnapping;
import org.bukkit.entity.Player;

public class KidnappingProvider extends BaseProvider {
    private final KidnappingBag kidnappingBag;

    public KidnappingProvider() {
        kidnappingBag = (KidnappingBag)bagManager.getBag("KidnappingBag");
    }

    public boolean isKidnapper(Player kidnapper) {
        return kidnappingBag.getData().containsKey(kidnapper);
    }

    public boolean isKidnapped(Player kidnapped) {
        return kidnappingBag.getData().containsValue(kidnapped);
    }

    public void saveKidnapping(Player kidnapper, Player kidnapped) {
        kidnappingBag.getData().put(kidnapper, kidnapped);
    }

    public void removeKidnapping(Player kidnapper) {
        kidnappingBag.getData().remove(kidnapper);
    }

    public Kidnapping getFromKidnapper(Player kidnapper) {
        if(!isKidnapper(kidnapper))
            return null;

        return new Kidnapping(kidnapper, kidnappingBag.getData().get(kidnapper));
    }

    public Kidnapping getFromKidnapped(Player kidnapped) {
        if(!isKidnapped(kidnapped))
            return null;

        for(Player curKidnapper : kidnappingBag.getData().keySet())
            if(kidnappingBag.getData().get(curKidnapper).equals(kidnapped))
                return new Kidnapping(curKidnapper, kidnapped);

        return null;
    }
}
