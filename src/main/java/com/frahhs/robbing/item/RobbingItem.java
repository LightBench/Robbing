package com.frahhs.robbing.item;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.provider.ConfigProvider;
import com.frahhs.robbing.provider.MessagesProvider;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class representing a custom robbing item.
 */
public abstract class RobbingItem {
    protected final Robbing plugin;
    protected final ConfigProvider configProvider;
    protected final MessagesProvider messagesProvider;

    protected ItemStack item;

    /**
     * Constructor for RobbingItem.
     */
    protected RobbingItem(Robbing plugin) {
        this.plugin = plugin;
        this.configProvider = plugin.getConfigProvider();
        this.messagesProvider = plugin.getMessagesProvider();

        if(getVanillaMaterial().isBlock())
            item = new ItemStack(Material.BARRIER, 1);
        else
            item = new ItemStack(getVanillaMaterial(), 1);

        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.WHITE + messagesProvider.getMessage(getName() + ".name", false));
        List<String> lore = new ArrayList<>();
        String[] loreStrings = messagesProvider.getMessage(getName() + ".lore", false).split("\n");

        for(String cur : loreStrings)
            lore.add(ChatColor.GRAY + cur);

        meta.setLore(lore);
        meta.setCustomModelData(getCustomModelData());
        item.setItemMeta(meta);
    }

    /**
     * Retrieves the ItemStack of the custom robbing item.
     *
     * @return The ItemStack of the custom robbing item.
     */
    public ItemStack getItemStack() {
        return item.clone();
    }

    /**
     * Abstract method to get the name of the custom robbing item.
     * The name must be unique.
     */
    public String getName() {
        return getRobbingMaterial().toString().toLowerCase();
    }

    /**
     * Retrieves the shaped recipe of the custom robbing item.
     *
     * @return The shaped recipe of the custom robbing item.
     */
    public abstract ShapedRecipe getShapedRecipe();

    /**
     * Retrieves the namespaced key of the custom robbing item.
     *
     * @return The namespaced key of the custom robbing item.
     */
    @NotNull
    public abstract NamespacedKey getNamespacedKey();

    /**
     * Abstract method to get the RobbingMaterial of the custom robbing item.
     */
    @NotNull
    public abstract RobbingMaterial getRobbingMaterial();

    /**
     * Abstract method to determine if the custom robbing item is craftable.
     * TODO: make customizable
     */
    public abstract boolean isCraftable();

    /**
     * Abstract method to get the name of the custom robbing item.
     * The name must be unique.
     */
    @NotNull
    public abstract Material getVanillaMaterial();

    /**
     * Abstract method to get the name of the custom robbing item.
     * The name must be unique.
     */
    public abstract int getCustomModelData();
}
