package com.frahhs.robbing.item;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.RobbingListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

public class CustomRecipesListener extends RobbingListener {

    @EventHandler
    public void onCraft(CraftItemEvent e) {
        Player player = (Player) e.getWhoClicked();
        ItemStack item = e.getRecipe().getResult();

        ItemManager itemManager = Robbing.getInstance().getItemsManager();

        for(RobbingItem cur : itemManager.getRegisteredItems()) {
            // Check if cur custom item is craftable
            if(cur.isCraftable()) {
                // Check if it is a custom item
                if (cur.getItemStack().isSimilar(item)) {
                    // Check if player have permission
                    if (!player.hasPermission(String.format("robbing.craft.%s", cur.getName().toLowerCase()))) {
                        String message = messages.getMessage("general.no_permissions");
                        player.sendMessage(message);
                        e.setCancelled(true);
                    }
                }
            }
        }
    }
}
