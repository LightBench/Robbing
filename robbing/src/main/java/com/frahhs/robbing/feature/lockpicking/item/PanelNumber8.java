package com.frahhs.robbing.feature.lockpicking.item;

import com.frahhs.lightlib.item.LightItem;
import org.bukkit.Material;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;

public class PanelNumber8 extends LightItem {
    @Override
    public ShapedRecipe getDefaultShapedRecipe() {
        return null;
    }

    @Override
    public boolean isGivable() {
        return false;
    }

    @Override
    public boolean isUnique() {
        return false;
    }

    @Override
    public @NotNull Material getVanillaMaterial() {
        return Material.STICK;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "panel_number_8";
    }

    @Override
    public int getCustomModelData() {
        return 5468;
    }
}
