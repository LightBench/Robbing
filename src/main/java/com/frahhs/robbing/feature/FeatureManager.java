package com.frahhs.robbing.feature;

import com.frahhs.robbing.Robbing;

import java.util.HashMap;
import java.util.Map;

/**
 * Class responsible for managing features within the plugin.
 */
public class FeatureManager {
    protected Robbing plugin;
    private final Map<String, Feature> features;

    /**
     * Constructs a FeatureManager object.
     *
     * @param plugin The main plugin instance.
     */
    public FeatureManager(Robbing plugin) {
        this.plugin = plugin;
        features = new HashMap<>();
    }

    /**
     * Registers a feature with the manager.
     *
     * @param feature The feature to register.
     */
    public void registerFeatures(Feature feature) {
        features.put(feature.getID(), feature);
        feature.registerEvents();
        feature.registerBags();
        feature.onEnable();
    }

    /**
     * Retrieves a feature by its ID.
     *
     * @param id The ID of the feature.
     * @return The feature with the given ID, or null if not found.
     */
    public Feature getFeature(String id) {
        return features.get(id);
    }

    /**
     * Enables all registered features.
     */
    public void enableFeatures() {
        try {
            for (Feature feature : features.values()) {
                Robbing.getRobbingLogger().finer("Enabling %s feature", feature.getID());
                feature.onEnable();
            }
        } catch (Error e) {
            Robbing.getRobbingLogger().error("Error while enabling a feature.\nStackTrace: \n%s", e.getMessage());
        }
    }

    /**
     * Disables all registered features.
     */
    public void disableFeatures() {
        try {
            for (Feature feature : features.values()) {
                Robbing.getRobbingLogger().finer("Disabling %s feature", feature.getID());
                feature.onDisable();
            }
        } catch (Exception e) {
            Robbing.getRobbingLogger().error("Error while disabling a feature.\nStackTrace: \n%s", e.getMessage());
        }
    }
}
