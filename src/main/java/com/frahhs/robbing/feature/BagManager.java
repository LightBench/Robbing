package com.frahhs.robbing.feature;

import java.util.HashMap;
import java.util.Map;

public class BagManager {
    private final Map<String, Bag> bags;

    public BagManager() {
        bags = new HashMap<>();
    }

    public void registerBags(Bag bag) {
        bags.put(bag.getID(), bag);
        bag.onEnable();
    }

    public Bag getBag(String id) {
        return bags.get(id);
    }

    public void enableBags() {
        for(Bag bag : bags.values()) {
            bag.onEnable();
        }
    }

    public void disableBags() {
        for(Bag bag : bags.values()) {
            bag.onDisable();
        }
    }
}
