package com.frahhs.robbing.item.items;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.item.RobbingItem;
import com.frahhs.robbing.item.RobbingMaterial;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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
    public @NotNull RobbingMaterial getRobbingMaterial() {
        return RobbingMaterial.SAFE;
    }

    @Override
    public boolean isCraftable() {
        return false;
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
        return 5458;
    }

    @Override
    public List<String> getLore() {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Status: " + ChatColor.DARK_GREEN + "Free");

        return lore;
    }
}
