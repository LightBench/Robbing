package com.frahhs.robbing.items.rbitems;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.items.RBItem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Handcuffs extends RBItem {
    public Handcuffs() {
        super();
    }

    @Override
    protected void setItemStack() {
        item = new ItemStack(Material.LEAD, 1);

        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName("§e" + messagesManager.getMessage("handcuffs.name", false));
        List<String> lore = new ArrayList<>();

        String[] loreStrings = messagesManager.getMessage("handcuffs.lore", false).split("\n");

        for(String cur : loreStrings)
            lore.add("§7" + cur);

        meta.setLore(lore);
        meta.setCustomModelData(5456);
        item.setItemMeta(meta);
    }

    @Override
    public void setShapedRecipe() {
        namespacedKey = new NamespacedKey(Robbing.getInstance(), "Handcuffs");
        shapedRecipe = new ShapedRecipe(namespacedKey, item);

        shapedRecipe.shape("   ", "BIB", "   ");
        shapedRecipe.setIngredient('I', Material.IRON_INGOT);
        shapedRecipe.setIngredient('B', Material.IRON_BLOCK);
    }
}
