package com.frahhs.robbing.item.items;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.item.RobbingItem;
import com.frahhs.robbing.item.RobbingMaterial;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;

public class HandcuffsKey extends RobbingItem {
    public HandcuffsKey(Robbing plugin) {
        super(plugin);
    }

    @Override
    public ShapedRecipe getShapedRecipe() {
        ShapedRecipe shapedRecipe = new ShapedRecipe(getNamespacedKey(), getItemStack());

        shapedRecipe.shape(" I ", " I ", " I ");
        shapedRecipe.setIngredient('I', Material.IRON_INGOT);

        return shapedRecipe;
    }

    @Override
    public @NotNull NamespacedKey getNamespacedKey() {
        return new NamespacedKey(plugin, "handcuffs_key");
    }

    @Override
    public @NotNull RobbingMaterial getRobbingMaterial() {
        return RobbingMaterial.HANDCUFFS_KEY;
    }

    @Override
    public boolean isCraftable() {
        return configProvider.getBoolean("handcuffing.enable-crafting.handcuffs_key");
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
