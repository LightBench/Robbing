package com.frahhs.robbing.item.items;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.item.RobbingItem;
import com.frahhs.robbing.item.RobbingMaterial;
import org.bukkit.Material;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;

public class CylinderCorrect extends RobbingItem {
    @Override
    public ShapedRecipe getDefaultShapedRecipe() {
        return null;
    }

    @Override
    public @NotNull RobbingMaterial getRobbingMaterial() {
        return RobbingMaterial.CYLINDER_CORRECT;
    }

    @Override
    public boolean isGivable() {
        return false;
    }

    @Override
    public @NotNull Material getVanillaMaterial() {
        return Material.GREEN_WOOL;
    }

    @Override
    public int getCustomModelData() {
        return 5474;
    }
}
