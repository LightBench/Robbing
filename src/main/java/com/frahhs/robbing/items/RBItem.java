package com.frahhs.robbing.items;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.managers.ConfigManager;
import com.frahhs.robbing.managers.MessagesManager;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

/**
 * Abstract class representing a custom robbing item.
 */
public abstract class RBItem {

    protected ItemStack item;
    protected ShapedRecipe shapedRecipe;
    protected NamespacedKey namespacedKey;
    protected final ConfigManager configManager;
    protected final MessagesManager messagesManager;

    /**
     * Constructor for RBItem.
     */
    protected RBItem() {
        configManager = Robbing.getInstance().getConfigManager();
        messagesManager = Robbing.getInstance().getMessagesManager();
        setItemStack();
        setShapedRecipe();
    }

    /**
     * Retrieves the ItemStack of the custom robbing item.
     *
     * @return The ItemStack of the custom robbing item.
     */
    public ItemStack getItemStack() {
        return item;
    }

    /**
     * Abstract method to set the ItemStack for the custom robbing item.
     */
    protected abstract void setItemStack();

    /**
     * Abstract method to get the name of the custom robbing item.
     */
    public abstract String getItemName();

    /**
     * Abstract method to get the RBMaterial of the custom robbing item.
     */
    public abstract RBMaterial getRBMaterial();

    /**
     * Abstract method to determine if the custom robbing item is craftable.
     */
    public abstract boolean isCraftable();

    /**
     * Abstract method to set the shaped recipe for the custom robbing item.
     */
    protected abstract void setShapedRecipe();

    /**
     * Retrieves the shaped recipe of the custom robbing item.
     *
     * @return The shaped recipe of the custom robbing item.
     */
    public ShapedRecipe getShapedRecipe() {
        return shapedRecipe;
    }

    /**
     * Retrieves the namespaced key of the custom robbing item.
     *
     * @return The namespaced key of the custom robbing item.
     */
    public NamespacedKey getNamespacedKey() {
        return namespacedKey;
    }
}
