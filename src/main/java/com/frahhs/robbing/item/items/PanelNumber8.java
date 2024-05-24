package com.frahhs.robbing.item.items;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.item.RobbingItem;
import com.frahhs.robbing.item.RobbingMaterial;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;

public class PanelNumber8 extends RobbingItem {
    public PanelNumber8(Robbing plugin) {
        super(plugin);
    }

    @Override
    public ShapedRecipe getShapedRecipe() {
        return null;
    }

    @Override
    public @NotNull NamespacedKey getNamespacedKey() {
        return new NamespacedKey(plugin, "PanelNumber8");
    }

    @Override
    public @NotNull RobbingMaterial getRobbingMaterial() {
        return RobbingMaterial.PANEL_NUMBER_8;
    }

    @Override
    public boolean isCraftable() {
        return false;
    }

    @Override
    public @NotNull Material getVanillaMaterial() {
        return Material.STICK;
    }

    @Override
    public int getCustomModelData() {
        return 5468;
    }
}
