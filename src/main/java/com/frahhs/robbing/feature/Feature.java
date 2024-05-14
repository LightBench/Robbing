package com.frahhs.robbing.feature;

import com.frahhs.robbing.Robbing;

public abstract class Feature {
    protected abstract void onEnable();

    protected abstract void onDisable();

    protected abstract void registerEvents();

    protected abstract void registerBags();

    protected abstract String getID();
}
