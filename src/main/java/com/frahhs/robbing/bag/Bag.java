package com.frahhs.robbing.bag;

import java.util.Map;

abstract public class Bag {
    /**
     * Called when the plugin is enabled.
     */
    protected abstract void onEnable();

    /**
     * Called when the plugin is disabled.
     */
    protected abstract void onDisable();

    protected abstract String getID();

    protected abstract Map getMap();
}
