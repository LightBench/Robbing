package com.frahhs.robbing.feature.handcuffing.item;

import com.frahhs.lightlib.item.LightItem;
import org.bukkit.Material;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;

public class HandcuffsKey extends LightItem {
    @Override
    public ShapedRecipe getDefaultShapedRecipe() {
        ShapedRecipe shapedRecipe = new ShapedRecipe(getNamespacedKey(), getItemStack());

        shapedRecipe.shape(" I ", " I ", " I ");
        shapedRecipe.setIngredient('I', Material.IRON_INGOT);

        return shapedRecipe;
    }

    @Override
    public boolean isGivable() {
        return true;
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
        return "handcuffs_key";
    }

    @Override
    public int getCustomModelData() {
        return 5459;
    }
}
