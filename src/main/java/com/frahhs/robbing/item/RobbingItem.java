package com.frahhs.robbing.item;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.providers.ConfigProvider;
import com.frahhs.robbing.providers.MessagesProvider;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;

/**
 * Abstract class representing a custom robbing item.
 */
public abstract class RobbingItem {
    protected final ConfigProvider configProvider = Robbing.getInstance().getConfigProvider();
    protected final MessagesProvider messagesProvider = Robbing.getInstance().getMessagesProvider();

    /**
     * Constructor for RBItem.
     */
    protected RobbingItem() {}

    /**
     * Retrieves the ItemStack of the custom robbing item.
     *
     * @return The ItemStack of the custom robbing item.
     */
    @NotNull
    public abstract ItemStack getItemStack();

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
    public abstract NamespacedKey getNamespacedKey();

    /**
     * Abstract method to get the name of the custom robbing item.
     * The name must be unique.
     */
    public abstract String getItemName();

    /**
     * Abstract method to get the RBMaterial of the custom robbing item.
     */
    public abstract RobbingMaterial getRBMaterial();

    /**
     * Abstract method to determine if the custom robbing item is craftable.
     */
    public abstract boolean isCraftable();
}
