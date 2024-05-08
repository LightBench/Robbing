package com.frahhs.robbing.features.generics.listeners;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.features.BaseListener;
import com.frahhs.robbing.item.ItemsManager;
import com.frahhs.robbing.item.RobbingItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

public class CustomRecipesListener extends BaseListener {

    @EventHandler
    public void onCraft(CraftItemEvent e) {
        Player player = (Player) e.getWhoClicked();
        ItemStack item = e.getRecipe().getResult();

        ItemsManager itemsManager = Robbing.getInstance().getItemsManager();

        for(RobbingItem cur : itemsManager.getRegisteredItems()) {
            // Check if cur custom item is craftable
            if(cur.isCraftable()) {
                // Check if it is a custom item
                if (cur.getItemStack().isSimilar(item)) {
                    // Check if player have permission
                    if (!player.hasPermission(String.format("robbing.craft.%s", cur.getItemName().toLowerCase()))) {
                        String message = messages.getMessage("general.no_permissions");
                        player.sendMessage(message);
                        e.setCancelled(true);
                    }
                }
            }
        }
    }
}
