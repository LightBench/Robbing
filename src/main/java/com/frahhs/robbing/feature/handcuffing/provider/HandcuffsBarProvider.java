package com.frahhs.robbing.feature.handcuffing.provider;

import com.frahhs.robbing.feature.BaseProvider;
import com.frahhs.robbing.feature.handcuffing.bag.HandcuffsBarBag;
import com.frahhs.robbing.feature.handcuffing.model.HandcuffsBar;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HandcuffsBarProvider extends BaseProvider {
    private final HandcuffsBarBag handcuffsBarBag;

    public HandcuffsBarProvider() {
        handcuffsBarBag = (HandcuffsBarBag)bagManager.getBag("HandcuffsBarBag");
    }

    /**
     * Checks if a player has an active handcuffs health bar.
     *
     * @return True if the player has an active health bar, otherwise false.
     */
    public boolean haveHandcuffsBar(Player player) {
        return handcuffsBarBag.getData().containsKey(player);
    }

    /**
     * Retrieves the health bar associated with a player.
     *
     * @param player The player.
     * @return The health bar associated with the player.
     */
    public HandcuffsBar getHandcuffsBar(Player player) {
        return new HandcuffsBar(player, handcuffsBarBag.getData().get(player));
    }

    /**
     * Adds the handcuffs bar to a player.
     */
    public void saveHandcuffsBar(Player player, BossBar bar) {
        handcuffsBarBag.getData().put(player, bar);
    }

    /**
     * Removes the handcuffs bar from a player.
     */
    public void removeHandcuffsBar(Player player) {
        handcuffsBarBag.getData().remove(player);
    }

    public boolean isHandcuffsBar(BossBar bar) {
        for(BossBar curBar : handcuffsBarBag.getData().values())
            if(curBar.equals(bar))
                return true;

        return false;
    }

    public List<HandcuffsBar> getAllHandcuffsBar() {
        List<HandcuffsBar> bars = new ArrayList<>();
        for(Player player : handcuffsBarBag.getData().keySet())
            bars.add(new HandcuffsBar(player, handcuffsBarBag.getData().get(player)));

        return bars;
    }

}
