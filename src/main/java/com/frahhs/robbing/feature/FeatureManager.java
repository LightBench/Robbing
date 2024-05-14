package com.frahhs.robbing.feature;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.bag.Bag;

import java.util.HashMap;
import java.util.Map;

public class FeatureManager {
    protected Robbing plugin;
    private final Map<String, Feature> features;

    public FeatureManager(Robbing plugin) {
        this.plugin = plugin;
        features = new HashMap<>();
    }

    public void registerFeatures(Feature feature) {
        features.put(feature.getID(), feature);
        feature.registerEvents();
        feature.registerBags();
        feature.onEnable();
    }

    public Feature getFeature(String id) {
        return features.get(id);
    }

    public void enableFeatures() {
        for(Feature feature : features.values()) {
            feature.onEnable();
        }
    }

    public void disableFeatures() {
        for(Feature feature : features.values()) {
            feature.onDisable();
        }
    }
}
