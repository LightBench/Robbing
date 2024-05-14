package com.frahhs.robbing.util;

import com.frahhs.robbing.feature.BaseModel;

/**
 * Model class representing a cooldown for an action.
 */
public class Cooldown extends BaseModel {
    private long timestamp;
    private int cooldown;

    /**
     * Constructs a Cooldown instance with the specified timestamp and cooldown duration.
     *
     * @param timestamp The timestamp when the cooldown started.
     * @param cooldown The duration of the cooldown in seconds.
     */
    public Cooldown(long timestamp, int cooldown) {
        this.timestamp = timestamp;
        this.cooldown = cooldown;
    }

    /**
     * Sets the duration of the cooldown.
     *
     * @param cooldown The duration of the cooldown in seconds.
     */
    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    /**
     * Retrieves the duration of the cooldown.
     *
     * @return The duration of the cooldown in seconds.
     */
    public int getCooldown() {
        return cooldown;
    }

    /**
     * Sets the timestamp when the cooldown started.
     *
     * @param timestamp The timestamp when the cooldown started.
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Retrieves the timestamp when the cooldown started.
     *
     * @return The timestamp when the cooldown started.
     */
    public long getTimestamp() {
        return timestamp;
    }
}
