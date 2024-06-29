package com.frahhs.robbing.item;

import com.frahhs.robbing.Robbing;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Class for managing custom items related to robbing mechanics.
 */
public class ItemManager {
    private final JavaPlugin plugin;
    private final Map<String, RobbingItem> rbItems;

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
     * Registers a custom RobbingItem.
     *
     * @param rbItem The RobbingItem to register.
     */
    public void registerItems(RobbingItem rbItem, Robbing plugin) {
        if (rbItems.containsKey(rbItem.getName())) {
            throw new RuntimeException(String.format("Item name [%s] already exists.", rbItem.getName()));
        }

        rbItems.put(rbItem.getName(), rbItem);
        if (rbItem.isCraftable()) {
            Robbing.getRobbingLogger().finer("Adding %s shaped recipe.", rbItem.getName());
            plugin.getServer().addRecipe(rbItem.getShapedRecipe(plugin));
        }
    }

    /**
     * Dispose all registered items.
     */
    public void dispose() {
        for (String key : rbItems.keySet()) {
            if (rbItems.get(key).isCraftable()) {
                Robbing.getRobbingLogger().finer("Removing %s shaped recipe.", rbItems.get(key).getName());
                plugin.getServer().removeRecipe(rbItems.get(key).getNamespacedKey());
            }
        }
        rbItems.clear();
    }

    /**
     * Retrieves an ItemStack based on RobbingMaterial.
     *
     * @param robbingMaterial The RobbingMaterial to retrieve.
     * @return The corresponding RobbingItem, or null if no matching RobbingItem is found.
     */
    public RobbingItem get(RobbingMaterial robbingMaterial) {
        for (RobbingItem item : rbItems.values()) {
            if (item.getRobbingMaterial().equals(robbingMaterial)) {
                return item;
            }
        }
        return null;
    }

    /**
     * Retrieves a RobbingItem based on the provided ItemStack.
     *
     * @param itemStack The ItemStack to match.
     * @return The corresponding RobbingItem, or null if no matching RobbingItem is found.
     */
    public RobbingItem get(ItemStack itemStack) {
        ItemStack item = clean(itemStack);

        for (RobbingItem curItem : rbItems.values()) {
            ItemStack item2 = clean(curItem.getItemStack());
            if (item2.isSimilar(item)) {
                return curItem;
            }
        }
        return null;
    }

    /**
     * Retrieves all registered RobbingItem.
     *
     * @return A collection of all registered RobbingItem.
     */
    public Set<RobbingItem> getRegisteredItems() {
        return new HashSet<>(rbItems.values());
    }

    /**
     * Checks if the provided ItemStack is registered as a custom item.
     *
     * @param itemStack The ItemStack to check.
     * @return True if the ItemStack is registered, otherwise false.
     */
    public boolean isRegistered(ItemStack itemStack) {
        if(itemStack == null)
            return false;

        ItemStack item = clean(itemStack);

        for (RobbingItem curItem : rbItems.values()) {
            ItemStack item2 = clean(curItem.getItemStack());
            if (item2.isSimilar(item)) {
                return true;
            }
        }
        return false;
    }

    private ItemStack clean(ItemStack itemStack) {
        ItemStack item = itemStack.clone();

        ItemMeta meta = item.getItemMeta();
        assert meta != null;

        // Remove PersistentDataContainer keys from the ItemStack
        PersistentDataContainer container = meta.getPersistentDataContainer();
        for(NamespacedKey key : container.getKeys())
            container.remove(key);

        // Remove Lore the ItemStack
        meta.setLore(null);

        item.setItemMeta(meta);
        return item;
    }
}
