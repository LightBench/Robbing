package com.frahhs.robbing.item;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Class for managing custom items related to robbing mechanics.
 */
public class ItemManager {
    private final JavaPlugin plugin;
    private Map<String, RobbingItem> rbItems;

    /**
     * Constructor for ItemManager.
     *
     * @param plugin The JavaPlugin instance.
     */
    public ItemManager(JavaPlugin plugin) {
        this.plugin = plugin;

        // Initialize the items map
        rbItems = new HashMap<>();
        plugin.getServer().getPluginManager().registerEvents(new CustomRecipesListener(), plugin);
    }

    /**
     * Registers a custom RBItem.
     *
     * @param rbItem The RBItem to register.
     */
    public void registerItem(RobbingItem rbItem) {
        if (rbItems.containsKey(rbItem.getItemName())) {
            throw new RuntimeException(String.format("Item name [%s] already exists.", rbItem.getItemName()));
        }

        rbItems.put(rbItem.getItemName(), rbItem);
        if (rbItem.isCraftable()) {
            plugin.getServer().addRecipe(rbItem.getShapedRecipe());
        }
    }

    /**
     * Dispose all registered items.
     */
    public void dispose() {
        for (String key : rbItems.keySet()) {
            if (rbItems.get(key).isCraftable()) {
                plugin.getServer().removeRecipe(rbItems.get(key).getNamespacedKey());
            }
        }
        rbItems.clear();
    }

    /**
     * Retrieves an ItemStack based on RBMaterial.
     *
     * @param robbingMaterial The RBMaterial to retrieve.
     * @return The corresponding RobbingItem, or null if no matching RBItem is found.
     */
    public RobbingItem get(RobbingMaterial robbingMaterial) {
        for (RobbingItem item : rbItems.values()) {
            if (item.getRBMaterial().equals(robbingMaterial)) {
                return item;
            }
        }
        return null;
    }

    /**
     * Retrieves an ItemStack based on item name.
     *
     * @param itemName The name of the item to retrieve.
     * @return The corresponding RobbingItem, or null if no matching RBItem is found.
     */
    public RobbingItem getByName(String itemName) {
        for (RobbingItem item : rbItems.values()) {
            if (item.getItemName().equalsIgnoreCase(itemName)) {
                return item;
            }
        }
        return null;
    }

    /**
     * Retrieves a RobbingItem based on the provided ItemStack.
     *
     * @param itemStack The ItemStack to match.
     * @return The corresponding RobbingItem, or null if no matching RBItem is found.
     */
    public RobbingItem getByItemStack(ItemStack itemStack) {
        for (RobbingItem item : rbItems.values()) {
            if (item.getItemStack().isSimilar(itemStack)) {
                return item;
            }
        }
        return null;
    }

    /**
     * Retrieves all registered RBItems.
     *
     * @return A collection of all registered RBItems.
     */
    public Collection<RobbingItem> getRegisteredItems() {
        return rbItems.values();
    }

    /**
     * Checks if the provided ItemStack is registered as a custom item.
     *
     * @param itemStack The ItemStack to check.
     * @return True if the ItemStack is registered, otherwise false.
     */
    public boolean isRegistered(ItemStack itemStack) {
        for (RobbingItem item : rbItems.values()) {
            if (item.getItemStack().isSimilar(itemStack)) {
                return true;
            }
        }
        return false;
    }
}
