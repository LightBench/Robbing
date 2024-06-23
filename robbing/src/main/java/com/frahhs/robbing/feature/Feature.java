package com.frahhs.robbing.feature;

import org.jetbrains.annotations.NotNull;

/**
 * Abstract class representing a feature in the plugin.
 */
public abstract class Feature {

    /**
     * Called when the feature is enabled.
     */
    protected abstract void onEnable();

    /**
     * Called when the feature is disabled.
     */
    protected abstract void onDisable();

    /**
     * Registers events for the feature.
     */
    protected abstract void registerEvents();

    /**
     * Registers bags for the feature.
     */
    protected abstract void registerBags();

    /**
     * Retrieves the ID of the feature.
     *
     * @return The ID of the feature.
     */
    @NotNull
    protected abstract String getID();
}
