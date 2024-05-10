package com.frahhs.robbing.item.items;

import com.frahhs.robbing.item.RobbingItem;
import com.frahhs.robbing.item.RobbingMaterial;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Safe extends RobbingItem {
    @Override
    public @NotNull ItemStack getItemStack() {
        ItemStack item = new ItemStack(Material.BARRIER, 1);

        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName("§e" + messagesProvider.getMessage("safe.name", false));
        List<String> lore = new ArrayList<>();

        String[] loreStrings = messagesProvider.getMessage("safe.lore", false).split("\n");

        for(String cur : loreStrings)
            lore.add("§7" + cur);

        meta.setLore(lore);
        meta.setCustomModelData(5458);
        item.setItemMeta(meta);

        return item;
    }

    @Override
    public ShapedRecipe getShapedRecipe() {
        return null;
    }

    @Override
    public NamespacedKey getNamespacedKey() {
        return null;
    }

    @Override
    public String getItemName() {
        return "safe";
    }

    @Override
    public RobbingMaterial getRBMaterial() {
        return RobbingMaterial.SAFE;
    }

    @Override
    public boolean isCraftable() {
        return false;
    }
}
