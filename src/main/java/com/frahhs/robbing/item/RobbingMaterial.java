package com.frahhs.robbing.item;

import com.google.common.base.Preconditions;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Enum representing the custom Robbing materials.
 */
public enum RobbingMaterial {
    HANDCUFFS,
    HANDCUFFS_KEY,
    LOCKPICK,
    SAFE,
    PANEL_NUMBER_0,
    PANEL_NUMBER_1,
    PANEL_NUMBER_2,
    PANEL_NUMBER_3,
    PANEL_NUMBER_4,
    PANEL_NUMBER_5,
    PANEL_NUMBER_6,
    PANEL_NUMBER_7,
    PANEL_NUMBER_8,
    PANEL_NUMBER_9;

    public boolean isItem() {
        switch (this) {
            case HANDCUFFS:
            case HANDCUFFS_KEY:
            case LOCKPICK:
                return true;
            default:
                return false;
        }
    }

    public boolean isBlock() {
        switch (this) {
            case SAFE:
                return true;
            default:
                return false;
        }
    }

    @NotNull
    public static RobbingMaterial matchMaterial(@NotNull final String name) {
        String filtered = name;
        if (filtered.startsWith(NamespacedKey.MINECRAFT + ":")) {
            filtered = filtered.substring((NamespacedKey.MINECRAFT + ":").length());
        }

        filtered = filtered.toUpperCase();
        filtered = filtered.replaceAll("\\s+", "_").replaceAll("\\W", "");

        return RobbingMaterial.valueOf(filtered);
    }
}
