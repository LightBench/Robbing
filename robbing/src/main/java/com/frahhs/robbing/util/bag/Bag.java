package com.frahhs.robbing.util.bag;

/**
 * Abstract class representing a bag for storing plugin NOT persistent data.
 */
abstract public class Bag {
    /**
     * Called when the plugin is enabled.
     */
    protected abstract void onEnable();

    /**
     * Called when the plugin is disabled.
     */
    protected abstract void onDisable();

    /**
     * Retrieves the ID of the bag.
     *
     * @return The ID of the bag.
     */
    protected abstract String getID();

    /**
     * Retrieves the data stored in the bag.
     *
     * @return The data stored in the bag.
     */
    public abstract Object getData();
}
