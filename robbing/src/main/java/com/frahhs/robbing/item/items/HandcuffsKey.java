package com.frahhs.robbing.item.items;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.item.RobbingItem;
import com.frahhs.robbing.item.RobbingMaterial;
import org.bukkit.Material;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;

public class HandcuffsKey extends RobbingItem {
    @Override
    public ShapedRecipe getDefaultShapedRecipe() {
        ShapedRecipe shapedRecipe = new ShapedRecipe(getNamespacedKey(), getItemStack());

        shapedRecipe.shape(" I ", " I ", " I ");
        shapedRecipe.setIngredient('I', Material.IRON_INGOT);

        return shapedRecipe;
    }

    @Override
    public @NotNull RobbingMaterial getRobbingMaterial() {
        return RobbingMaterial.HANDCUFFS_KEY;
    }

    @Override
    public boolean isGivable() {
        return true;
    }

    @Override
    public @NotNull Material getVanillaMaterial() {
        return Material.STICK;
    }

    @Override
    public int getCustomModelData() {
        return 5459;
    }
}
