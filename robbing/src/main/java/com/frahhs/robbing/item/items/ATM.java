package com.frahhs.robbing.item.items;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.item.RobbingItem;
import com.frahhs.robbing.item.RobbingMaterial;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ATM extends RobbingItem {
    @Override
    public ShapedRecipe getDefaultShapedRecipe() {
        return null;
    }

    @Override
    public @NotNull RobbingMaterial getRobbingMaterial() {
        return RobbingMaterial.ATM;
    }

    @Override
    public boolean isGivable() {
        return true;
    }

    @Override
    public @NotNull Material getVanillaMaterial() {
        return Material.IRON_BLOCK;
    }

    @Override
    public int getCustomModelData() {
        return 5480;
    }

    @Override
    public List<String> getLore() {
        return null;
    }
}
