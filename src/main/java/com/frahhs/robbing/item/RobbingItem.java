package com.frahhs.robbing.item;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.provider.ConfigProvider;
import com.frahhs.robbing.provider.MessagesProvider;
import com.frahhs.robbing.util.recipe.RecipeManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.List;

/**
 * Abstract class representing a custom robbing item.
 */
public abstract class RobbingItem {
    protected final Robbing plugin;
    protected final ConfigProvider configProvider;
    protected final MessagesProvider messagesProvider;
    protected final NamespacedKey namespacedKey;

    protected ItemStack item;

    /**
     * Constructor for RobbingItem.
     */
    protected RobbingItem(Robbing plugin) {
        this.plugin = plugin;
        this.configProvider = plugin.getConfigProvider();
        this.messagesProvider = plugin.getMessagesProvider();
        this.namespacedKey = new NamespacedKey(plugin, getName());

        // TODO: Check if leave barrier here
        if(getRobbingMaterial().isBlock())
            item = new ItemStack(Material.BARRIER, 1);
        else
            item = new ItemStack(getVanillaMaterial(), 1);

        ItemMeta meta = item.getItemMeta();
        assert meta != null;

        // Display name
        meta.setDisplayName(ChatColor.WHITE + messagesProvider.getMessage("items_name."  + getName(), false));

        // Lore
        if(getLore() != null) {
            meta.setLore(getLore());
        }

        // Custom model data
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
     * Retrieves the Lore of the custom robbing item.
     *
     * @return The List<String> of the Lore of the custom robbing item, null if it has no lore.
     */
    public List<String> getLore() {
        return null;
    }

    /**
     * Retrieves the default shaped recipe of the custom robbing item.
     *
     * @return The shaped recipe of the custom robbing item.
     */
    public abstract ShapedRecipe getDefaultShapedRecipe();

    /**
     * Retrieves the saved shaped recipe of the custom robbing item.
     * If the plugin fail to select it, default shaped recipe will be returned
     *
     * @return The shaped recipe of the custom robbing item.
     */
    public ShapedRecipe getShapedRecipe() {
        RecipeManager recipeManager = new RecipeManager(plugin);

        ShapedRecipe shapedRecipe = getDefaultShapedRecipe();

        try {
            shapedRecipe = recipeManager.loadRecipe(this);
        } catch (SQLException e) {
            Robbing.getRobbingLogger().error("Error while selecting the recipe of %s from the database\n%s", getName(), e);
        }

        if(shapedRecipe == null)
            return getDefaultShapedRecipe();

        return shapedRecipe;
    }

    public void updateShapedRecipe(ShapedRecipe shapedRecipe) {
        if(!isCraftable()) {
            Robbing.getRobbingLogger().warning("Trying to update the recipe of a non craftable item: %s", getName());
            return;
        }

        RecipeManager recipeManager = new RecipeManager(plugin);

        try {
            recipeManager.saveRecipe(this, shapedRecipe);
        } catch (SQLException e) {
            Robbing.getRobbingLogger().error("Error while saving the recipe of %s from the database\n%s", getName(), e);
        }

        plugin.getServer().removeRecipe(getNamespacedKey());
        plugin.getServer().addRecipe(shapedRecipe);
    }

    /**
     * Retrieves the namespaced key of the custom robbing item.
     *
     * @return The namespaced key of the custom robbing item.
     */
    @NotNull
    public NamespacedKey getNamespacedKey() {
        return namespacedKey;
    }

    /**
     * Abstract method to get the RobbingMaterial of the custom robbing item.
     */
    @NotNull
    public abstract RobbingMaterial getRobbingMaterial();

    /**
     * Abstract method to determine if the custom robbing item is craftable.
     */
    public boolean isCraftable() {
        return getDefaultShapedRecipe() != null;
    }

    /**
     * Abstract method to determine if the custom robbing item can be given.
     */
    public abstract boolean isGivable();

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
