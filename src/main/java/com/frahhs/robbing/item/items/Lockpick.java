package com.frahhs.robbing.item.items;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.item.RobbingItem;
import com.frahhs.robbing.item.RobbingMaterial;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;

public class Lockpick extends RobbingItem {
    public Lockpick(Robbing plugin) {
        super(plugin);
    }

    @Override
    public ShapedRecipe getShapedRecipe() {
        ShapedRecipe shapedRecipe = new ShapedRecipe(getNamespacedKey(), getItemStack());

        shapedRecipe.shape(" I ", " S ", " I ");
        shapedRecipe.setIngredient('I', Material.IRON_INGOT);
        shapedRecipe.setIngredient('S', Material.STICK);

        return shapedRecipe;
    }

    @Override
    public @NotNull NamespacedKey getNamespacedKey() {
        return new NamespacedKey(plugin, "Lockpick");
    }

    @Override
    public @NotNull RobbingMaterial getRobbingMaterial() {
        return RobbingMaterial.LOCKPICK;
    }

    @Override
    public boolean isCraftable() {
        return configProvider.getBoolean("lockpicking.enable-crafting.lockpick");
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
        return 5457;
    }
}
