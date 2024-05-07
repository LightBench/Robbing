package com.frahhs.robbing.items.rbitems;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.items.BaseItem;
import com.frahhs.robbing.items.RBMaterial;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Lockpick extends BaseItem {
    public Lockpick() {
        super();
    }

    @Override
    protected void setItemStack() {
        item = new ItemStack(Material.STICK, 1);

        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName("§e" + messagesManager.getMessage("lockpick.name", false));
        List<String> lore = new ArrayList<>();

        String[] loreStrings = messagesManager.getMessage("lockpick.lore", false).split("\n");

        for(String cur : loreStrings)
            lore.add("§7" + cur);

        meta.setLore(lore);
        meta.setCustomModelData(5456);
        item.setItemMeta(meta);
    }

    @Override
    public String getItemName() {
        return "lockpick";
    }

    @Override
    public RBMaterial getRBMaterial() {
        return RBMaterial.LOCKPICK;
    }

    @Override
    public boolean isCraftable() {
        return configManager.getBoolean("lockpick.enable_crafting");
    }

    @Override
    public void setShapedRecipe() {
        namespacedKey = new NamespacedKey(Robbing.getInstance(), "Lockpick");
        shapedRecipe = new ShapedRecipe(namespacedKey, item);

        shapedRecipe.shape(" I ", " S ", " I ");
        shapedRecipe.setIngredient('I', Material.IRON_INGOT);
        shapedRecipe.setIngredient('S', Material.STICK);
    }
}
