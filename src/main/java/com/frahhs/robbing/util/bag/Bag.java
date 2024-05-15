package com.frahhs.robbing.util.bag;

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

    public abstract Object getData();
}
