package com.frahhs.robbing.item;

import com.frahhs.lightlib.item.LightItem;
import org.bukkit.Material;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;

public class Handcuffs extends LightItem {
    @Override
    public ShapedRecipe getDefaultShapedRecipe() {
        ShapedRecipe shapedRecipe = new ShapedRecipe(getNamespacedKey(), getItemStack());

        shapedRecipe.shape("   ", "BIB", "   ");
        shapedRecipe.setIngredient('I', Material.IRON_INGOT);
        shapedRecipe.setIngredient('B', Material.IRON_BLOCK);

        shapedRecipe.getShape();
        shapedRecipe.getIngredientMap();

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
        return "handcuffs";
    }

    @Override
    public int getCustomModelData() {
        return 5456;
    }
}
