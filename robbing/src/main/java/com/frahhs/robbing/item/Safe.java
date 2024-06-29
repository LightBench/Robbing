package com.frahhs.robbing.item;

import com.frahhs.lightlib.item.LightItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Safe extends LightItem {
    @Override
    public ShapedRecipe getDefaultShapedRecipe() {
        ShapedRecipe shapedRecipe = new ShapedRecipe(getNamespacedKey(), getItemStack());

        shapedRecipe.shape("III", "ICI", "III");
        shapedRecipe.setIngredient('C', Material.CHEST);
        shapedRecipe.setIngredient('I', Material.IRON_BLOCK);

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
        return Material.IRON_BLOCK;
    }

    @Override
    public int getCustomModelData() {
        return 5458;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "safe";
    }

    @Override
    public List<String> getLore() {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Status: " + ChatColor.DARK_GREEN + "Free");

        return lore;
    }
}
