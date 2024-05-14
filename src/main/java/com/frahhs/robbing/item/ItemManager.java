package com.frahhs.robbing.item;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Class for managing custom Items related to robbing mechanics.
 */
public class ItemManager {
    private final JavaPlugin plugin;
    private Map<String, RobbingItem> rbItems;

    /**
     * Constructor for ItemsManager.
     *
     * @param plugin The JavaPlugin instance.
     */
    public ItemManager(JavaPlugin plugin) {
        this.plugin = plugin;

        // Setup items map
        rbItems= new HashMap<>();
        plugin.getServer().getPluginManager().registerEvents(new CustomRecipesListener(), plugin);
    }

    /**
     * Registers a custom RBItem.
     *
     * @param rbItem The RBItem to register.
     */
    public void registerItem(RobbingItem rbItem) {
        //TODO: add custom exception here and more checks for the items validation
        if(rbItems.containsKey(rbItem.getItemName()))
            throw new RuntimeException(String.format("Item name [%s] already existing.", rbItem.getItemName()));

        rbItems.put(rbItem.getItemName(), rbItem);
        if(rbItem.isCraftable())
            plugin.getServer().addRecipe(rbItem.getShapedRecipe());
    }

    /**
     * Dispose all registered items.
     */
    public void dispose() {
        for (String key : rbItems.keySet())
            if(rbItems.get(key).isCraftable())
                plugin.getServer().removeRecipe(rbItems.get(key).getNamespacedKey());

        rbItems = new HashMap<>();
    }

    /**
     * Retrieves an ItemStack based on RBMaterial.
     *
     * @param robbingMaterial The RBMaterial to retrieve.
     * @return The corresponding ItemStack, or null if no matching RBItem is found.
     */
    public RobbingItem get(RobbingMaterial robbingMaterial) {
        for (String key : rbItems.keySet()) {
            if(rbItems.get(key).getRBMaterial().equals(robbingMaterial))
                return rbItems.get(key);
        }
        return null;
    }

    /**
     * Retrieves an ItemStack based on item name.
     *
     * @param itemName The name of the item to retrieve.
     * @return The corresponding ItemStack, or null if no matching RBItem is found.
     */
    public RobbingItem getByName(String itemName) {
        for (String key : rbItems.keySet())
            if(rbItems.get(key).getItemName().equalsIgnoreCase(itemName))
                return rbItems.get(key);
        return null;
    }

    public RobbingItem getByItemStack(ItemStack itemStack) {
        for(RobbingItem cur : getRegisteredItems())
            if(cur.getItemStack().isSimilar(itemStack))
                return cur;
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

    public boolean isRegistered(ItemStack itemStack) {
        for(RobbingItem cur : getRegisteredItems())
            if(cur.getItemStack().isSimilar(itemStack))
                return true;
        return false;
    }
}
