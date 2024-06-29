package com.frahhs.robbing.item;

import com.frahhs.lightlib.item.LightItem;
import org.bukkit.Material;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;

public class Cylinder extends LightItem {
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
        return Material.LIGHT_GRAY_WOOL;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "cylinder";
    }

    @Override
    public int getCustomModelData() {
        return 5472;
    }
}
