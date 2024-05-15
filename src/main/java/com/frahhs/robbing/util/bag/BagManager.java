package com.frahhs.robbing.util.bag;

import java.util.HashMap;
import java.util.Map;

/**
 * Class for managing bags storing plugin data.
 */
public class BagManager {
    private final Map<String, Bag> bags;

    /**
     * Constructs a BagManager.
     */
    public BagManager() {
        bags = new HashMap<>();
    }

    /**
     * Registers a bag.
     *
     * @param bag The bag to register.
     */
    public void registerBags(Bag bag) {
        bags.put(bag.getID(), bag);
        bag.onEnable();
    }

    /**
     * Retrieves a bag by its ID.
     *
     * @param id The ID of the bag to retrieve.
     * @return The bag corresponding to the ID.
     */
    public Bag getBag(String id) {
        return bags.get(id);
    }

    /**
     * Enables all registered bags.
     */
    public void enableBags() {
        for (Bag bag : bags.values()) {
            bag.onEnable();
        }
    }

    /**
     * Disables all registered bags.
     */
    public void disableBags() {
        for (Bag bag : bags.values()) {
            bag.onDisable();
        }
    }
}
