package com.frahhs.robbing.items;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.items.rbitems.Handcuffs;
import com.frahhs.robbing.items.rbitems.Lockpick;
import com.frahhs.robbing.managers.ConfigManager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Class for managing custom ItemStacks related to robbing mechanics.
 */
public class RBItemStack {

    private final JavaPlugin plugin;
    private final ConfigManager configManager;
    private final Handcuffs handcuffs;
    private final Lockpick lockpick;

    /**
     * Constructor for RBItemStack.
     *
     * @param plugin The main JavaPlugin instance.
     */
    public RBItemStack(JavaPlugin plugin) {
        this.plugin = plugin;
        configManager = Robbing.getInstance().getConfigManager();

        // Setup items
        handcuffs = new Handcuffs();
        lockpick = new Lockpick();

        // Assign recipes
        if(configManager.getBoolean("handcuffing.enable_crafting"))
            plugin.getServer().addRecipe(handcuffs.getShapedRecipe());
        if(configManager.getBoolean("lockpick.enable_crafting"))
            plugin.getServer().addRecipe(lockpick.getShapedRecipe());
    }

    /**
     * Disables the custom recipes associated with handcuffs and lockpicks.
     */
    public void disable() {
        plugin.getServer().removeRecipe(handcuffs.namespacedKey);
        plugin.getServer().removeRecipe(lockpick.namespacedKey);
    }

    /**
     * Retrieves the custom ItemStack corresponding to the given RBMaterial.
     *
     * @param rbMaterial The RBMaterial enum value representing the desired item.
     * @return The custom ItemStack corresponding to the given RBMaterial.
     * @throws RuntimeException if the RBMaterial is not found.
     */
    public ItemStack get(RBMaterial rbMaterial) {
        switch (rbMaterial) {
            case HANDCUFFS:
                return handcuffs.getItemStack();
            case LOCKPICK:
                return lockpick.getItemStack();
            default:
                throw new RuntimeException(String.format("Item %s not found!", rbMaterial));
        }
    }
}
