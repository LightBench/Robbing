package com.frahhs.robbing.managers;

import com.frahhs.robbing.items.BaseItem;
import com.frahhs.robbing.items.RBMaterial;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Class for managing custom ItemStacks related to robbing mechanics.
 */
public class ItemsManager {

    private final JavaPlugin plugin;
    private Map<String, BaseItem> rbItems;

    /**
     * Constructor for ItemsManager.
     *
     * @param plugin The JavaPlugin instance.
     */
    public ItemsManager(JavaPlugin plugin) {
        this.plugin = plugin;

        // Setup items map
        rbItems= new HashMap<>();
    }

    /**
     * Registers a custom RBItem.
     *
     * @param rbItem The RBItem to register.
     */
    public void registerItem(BaseItem rbItem) {
        rbItems.put(rbItem.getItemName(), rbItem);
        if(rbItem.isCraftable())
            plugin.getServer().addRecipe(rbItem.getShapedRecipe());
    }

    /**
     * Dispose all registered items.
     */
    public void dispose() {
        for (String key : rbItems.keySet()) {
            plugin.getServer().removeRecipe(rbItems.get(key).getNamespacedKey());
        }
        rbItems = new HashMap<>();
    }

    /**
     * Retrieves an ItemStack based on RBMaterial.
     *
     * @param rbMaterial The RBMaterial to retrieve.
     * @return The corresponding ItemStack, or null if no matching RBItem is found.
     */
    public ItemStack get(RBMaterial rbMaterial) {
        for (String key : rbItems.keySet()) {
            if(rbItems.get(key).getRBMaterial().equals(rbMaterial))
                return rbItems.get(key).getItemStack();
        }
        return null;
    }

    /**
     * Retrieves an ItemStack based on item name.
     *
     * @param itemName The name of the item to retrieve.
     * @return The corresponding ItemStack, or null if no matching RBItem is found.
     */
    public ItemStack getByName(String itemName) {
        for (String key : rbItems.keySet()) {
            if(rbItems.get(key).getItemName().equalsIgnoreCase(itemName))
                return rbItems.get(key).getItemStack();
        }
        return null;
    }

    /**
     * Retrieves all registered RBItems.
     *
     * @return A collection of all registered RBItems.
     */
    public Collection<BaseItem> getRegisteredItems() {
        return rbItems.values();
    }
}
