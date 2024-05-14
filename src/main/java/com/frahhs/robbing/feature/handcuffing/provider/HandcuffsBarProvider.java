package com.frahhs.robbing.feature.handcuffing.provider;

import com.frahhs.robbing.feature.handcuffing.bag.HandcuffsBarBag;
import com.frahhs.robbing.feature.BaseProvider;
import com.frahhs.robbing.feature.handcuffing.model.HandcuffsBar;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HandcuffsBarProvider extends BaseProvider {
    /**
     * Checks if a player has an active handcuffs health bar.
     *
     * @return True if the player has an active health bar, otherwise false.
     */
    public boolean haveHandcuffsBar(Player player) {
        HandcuffsBarBag handcuffsBarBag = (HandcuffsBarBag)bagManager.getBag("HandcuffsBarBag");
        return handcuffsBarBag.getMap().containsKey(player);
    }

    /**
     * Retrieves the health bar associated with a player.
     *
     * @param player The player.
     * @return The health bar associated with the player.
     */
    public BossBar getHandcuffsBar(Player player) {
        HandcuffsBarBag handcuffsBarBag = (HandcuffsBarBag)bagManager.getBag("HandcuffsBarBag");
        return handcuffsBarBag.getMap().get(player);
    }

    /**
     * Adds the handcuffs bar to a player.
     */
    public void saveHandcuffsBar(Player player, BossBar bar) {
        HandcuffsBarBag handcuffsBarBag = (HandcuffsBarBag)bagManager.getBag("HandcuffsBarBag");
        handcuffsBarBag.getMap().put(player, bar);
    }

    /**
     * Removes the handcuffs bar from a player.
     */
    public void removeHandcuffsBar(Player player) {
        HandcuffsBarBag handcuffsBarBag = (HandcuffsBarBag)bagManager.getBag("HandcuffsBarBag");
        handcuffsBarBag.getMap().remove(player);
    }

    public boolean isHandcuffsBar(BossBar bar) {
        HandcuffsBarBag handcuffsBarBag = (HandcuffsBarBag)bagManager.getBag("HandcuffsBarBag");

        for(BossBar curBar : handcuffsBarBag.getMap().values())
            if(curBar.equals(bar))
                return true;

        return false;
    }

    public List<HandcuffsBar> getAllHandcuffsBar() {
        HandcuffsBarBag handcuffsBarBag = (HandcuffsBarBag)bagManager.getBag("HandcuffsBarBag");

        List<HandcuffsBar> bars = new ArrayList<>();
        for(Player player : handcuffsBarBag.getMap().keySet())
            bars.add(new HandcuffsBar(player, handcuffsBarBag.getMap().get(player)));

        return bars;
    }

}
