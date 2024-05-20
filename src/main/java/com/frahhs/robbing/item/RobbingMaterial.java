package com.frahhs.robbing.item;

/**
 * Enum representing the custom Robbing materials.
 */
public enum RobbingMaterial {
    HANDCUFFS,
    HANDCUFFS_KEY,
    LOCKPICK,
    SAFE;

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
}
