package com.frahhs.robbing.item.items;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.item.RobbingItem;
import com.frahhs.robbing.item.RobbingMaterial;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;

public class CylinderWrong extends RobbingItem {
    public CylinderWrong(Robbing plugin) {
        super(plugin);
    }

    @Override
    public ShapedRecipe getDefaultShapedRecipe() {
        return null;
    }

    @Override
    public @NotNull RobbingMaterial getRobbingMaterial() {
        return RobbingMaterial.CYLINDER_WRONG;
    }

    @Override
    public boolean isGivable() {
        return false;
    }

    @Override
    public @NotNull Material getVanillaMaterial() {
        return Material.BLACK_WOOL;
    }

    @Override
    public int getCustomModelData() {
        return 5473;
    }
}
