package com.frahhs.robbing.item.items;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.item.RobbingItem;
import com.frahhs.robbing.item.RobbingMaterial;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;

public class Safe extends RobbingItem {
    public Safe(Robbing plugin) {
        super(plugin);
    }

    @Override
    public ShapedRecipe getShapedRecipe() {
        return null;
    }

    @Override
    public @NotNull NamespacedKey getNamespacedKey() {
        return new NamespacedKey(plugin, "Safe");
    }

    @Override
    public @NotNull String getItemName() {
        return "safe";
    }

    @Override
    public @NotNull RobbingMaterial getRBMaterial() {
        return RobbingMaterial.SAFE;
    }

    @Override
    public boolean isCraftable() {
        return false;
    }

    @Override
    public @NotNull Material getVanillaMaterial() {
        return Material.IRON_BLOCK;
    }

    @Override
    public int getCustomModelData() {
        return 5458;
    }
}
