package com.frahhs.robbing.items.rbitems;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.items.BaseItem;
import com.frahhs.robbing.items.RBMaterial;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Handcuffs extends BaseItem {

    @Override
    @NotNull
    public ItemStack getItemStack() {
        ItemStack item = new ItemStack(Material.LEAD, 1);

        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName("§e" + messagesProvider.getMessage("handcuffs.name", false));
        List<String> lore = new ArrayList<>();

        String[] loreStrings = messagesProvider.getMessage("handcuffs.lore", false).split("\n");

        for(String cur : loreStrings)
            lore.add("§7" + cur);

        meta.setLore(lore);
        meta.setCustomModelData(5456);
        item.setItemMeta(meta);

        return item;
    }

    @Override
    public ShapedRecipe getShapedRecipe() {
        NamespacedKey namespacedKey = getNamespacedKey();
        ShapedRecipe shapedRecipe = new ShapedRecipe(namespacedKey, getItemStack());

        shapedRecipe.shape("   ", "BIB", "   ");
        shapedRecipe.setIngredient('I', Material.IRON_INGOT);
        shapedRecipe.setIngredient('B', Material.IRON_BLOCK);

        return shapedRecipe;
    }

    @Override
    public NamespacedKey getNamespacedKey() {
        return new NamespacedKey(Robbing.getInstance(), "Handcuffs");
    }

    @Override
    public String getItemName() {
        return "handcuffs";
    }

    @Override
    public RBMaterial getRBMaterial() {
        return RBMaterial.HANDCUFFS;
    }

    @Override
    public boolean isCraftable() {
        return configProvider.getBoolean("handcuffing.enable_crafting");
    }
}
