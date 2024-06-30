package com.frahhs.robbing.feature.handcuffing.bag;

import com.frahhs.lightlib.util.bag.Bag;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Bag to store handcuffed now player to avoid the kidnap in the same action of handcuff.
 */
public class JustHandcuffBag extends Bag {
    private List<Player> justHandcuff;

    @Override
    protected void onEnable() {
        justHandcuff = new ArrayList<>();
    }

    @Override
    protected void onDisable() {
        justHandcuff = null;
    }

    @Override
    protected String getID() {
        return "JustHandcuffBag";
    }

    @Override
    public List<Player> getData() {
        return justHandcuff;
    }
}
