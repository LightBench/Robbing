package com.frahhs.robbing.menu;

import com.frahhs.lightlib.LightPlugin;
import com.frahhs.lightlib.item.ItemManager;
import com.frahhs.lightlib.item.LightItem;
import com.frahhs.lightlib.util.StringUtil;
import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.GuiPageElement;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Map;

public class RecipeMenu {
    public static void open(Player player, JavaPlugin plugin) {
        ItemManager itemManager = LightPlugin.getItemsManager();
        String[] guiSetup = {
                "         ",
                "  ggggg  ",
                "b fpdnl  "
        };

        InventoryGui gui = new InventoryGui(plugin, null, "Recipes (Page %page% of %pages%)", guiSetup);
        gui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE)); // fill the empty slots with this

        GuiElementGroup group = new GuiElementGroup('g');

        for(LightItem item : LightPlugin.getItemsManager().getRegisteredItems()) {
            if(item.isCraftable()) {
                ItemStack itemStack = item.getItemStack();
                ItemMeta meta = itemStack.getItemMeta();
                assert meta != null;
                meta.setLore(null);
                itemStack.setItemMeta(meta);

                group.addElement(new StaticGuiElement(
                        'e',
                        itemStack,
                        click -> {
                            openSpecificRecipe(item, player, plugin);
                            return true;
                        }
                ));
            }
        }

        gui.addElement(group);

        // First page
        gui.addElement(new GuiPageElement('f', new ItemStack(Material.ARROW), GuiPageElement.PageAction.FIRST, "Go to first page (current: %page%)"));

        // Previous page
        gui.addElement(new GuiPageElement('p', new ItemStack(Material.OAK_SIGN), GuiPageElement.PageAction.PREVIOUS, "Go to previous page (%prevpage%)"));

        // Next page
        gui.addElement(new GuiPageElement('n', new ItemStack(Material.OAK_SIGN), GuiPageElement.PageAction.NEXT, "Go to next page (%nextpage%)"));

        // Last page
        gui.addElement(new GuiPageElement('l', new ItemStack(Material.ARROW), GuiPageElement.PageAction.LAST, "Go to last page (%pages%)"));

        // Back to dashboard
        gui.addElement(new StaticGuiElement('b',
            new ItemStack(Material.BARRIER),
            click -> {
                player.closeInventory();
                DashboardMenu.open(player, plugin);
                return true; // returning true will cancel the click event and stop taking the item
            },
            ChatColor.WHITE + "Main page"
        ));

        player.closeInventory();
        gui.show(player);
    }

    private static void openSpecificRecipe(LightItem item, Player player, JavaPlugin plugin) {
        ItemManager itemManager = LightPlugin.getItemsManager();
        String[] guiSetup = {
                "         ",
                "   ggg   ",
                "   ggg   ",
                "   ggg   ",
                "b      cs"
        };

        InventoryGui gui = new InventoryGui(plugin, null,  StringUtil.capitalize(item.getName()).replace("_", " ") + " recipe", guiSetup);
        gui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE)); // fill the empty slots with this

        ShapedRecipe shapedRecipe = item.getShapedRecipe(plugin);
        String[] shape = shapedRecipe.getShape();
        Map<Character, ItemStack> ingredientMap = shapedRecipe.getIngredientMap();

        GuiElementGroup group = new GuiElementGroup('g');
        for(String row : shape) {
            for(char ingredient : row.toCharArray()) {
                if(ingredient == ' ') {
                    group.addElement(new StaticGuiElement('e', new ItemStack(Material.AIR), click -> false));
                } else {
                    group.addElement(new StaticGuiElement('e', ingredientMap.get(ingredient), click -> false));
                }
            }
        }
        gui.addElement(group);

        // Save
        gui.addElement(new StaticGuiElement('s',
                itemManager.get("panel_number_check").getItemStack(),
                click -> {
                    saveRecipe(player, item, plugin);
                    player.closeInventory();
                    return true; // returning true will cancel the click event and stop taking the item
                },
                ChatColor.BOLD + "" + ChatColor.DARK_GREEN + "Save recipe"
        ));

        // Cancel
        gui.addElement(new StaticGuiElement('c',
                itemManager.get("panel_number_cancel").getItemStack(),
                click -> {
                    player.closeInventory();
                    return true; // returning true will cancel the click event and stop taking the item
                },
                ChatColor.BOLD + "" + ChatColor.DARK_RED + "Restore recipe"
        ));

        // Back to recipes menu
        gui.addElement(new StaticGuiElement('b',
                new ItemStack(Material.BARRIER),
                click -> {
                    player.closeInventory();
                    open(player, plugin);
                    return true; // returning true will cancel the click event and stop taking the item
                },
                ChatColor.WHITE + "Items page"
        ));

        gui.show(player);
    }

    private static void saveRecipe(Player player, LightItem robbingItem, JavaPlugin plugin) {
        Inventory openInventory = player.getOpenInventory().getTopInventory();

        // Check if the player is not using only vanilla items
        for(int i = 0; i < 3; i++) {
            ItemStack item = openInventory.getItem(12 + i);
            if(LightPlugin.getItemsManager().isRegistered(item)) {
                player.closeInventory();
                String message = LightPlugin.getMessagesProvider().getMessage("general.recipe_not_vanilla");
                player.sendMessage(message);
                return;
            }
        }
        for(int i = 0; i < 3; i++) {
            ItemStack item = openInventory.getItem(21 + i);
            if(LightPlugin.getItemsManager().isRegistered(item)) {
                player.closeInventory();
                String message = LightPlugin.getMessagesProvider().getMessage("general.recipe_not_vanilla");
                player.sendMessage(message);
                return;
            }
        }
        for(int i = 0; i < 3; i++) {
            ItemStack item = openInventory.getItem(30 + i);
            if(LightPlugin.getItemsManager().isRegistered(item)) {
                player.closeInventory();
                String message = LightPlugin.getMessagesProvider().getMessage("general.recipe_not_vanilla");
                player.sendMessage(message);
                return;
            }
        }

        char[] chars = "abcdefghi".toCharArray();
        ShapedRecipe shapedRecipe = new ShapedRecipe(robbingItem.getNamespacedKey(), robbingItem.getItemStack());

        // First row
        StringBuilder row1 = new StringBuilder();
        for(int i = 0; i < 3; i++) {
            ItemStack item = openInventory.getItem(12 + i);
            if(item == null || item.getType() == Material.AIR) {
                row1.append(" ");
            } else {
                row1.append(chars[i]);
            }
        }

        // Second row
        StringBuilder row2 = new StringBuilder();
        for(int i = 0; i < 3; i++) {
            ItemStack item = openInventory.getItem(21 + i);
            if(item == null || item.getType() == Material.AIR) {
                row2.append(" ");
            } else {
                row2.append(chars[i + 3]);
            }
        }

        // Third row
        StringBuilder row3 = new StringBuilder();
        for(int i = 0; i < 3; i++) {
            ItemStack item = openInventory.getItem(30 + i);
            if(item == null || item.getType() == Material.AIR) {
                row3.append(" ");
            } else {
                row3.append(chars[i + 6]);
            }
        }

        shapedRecipe.shape(row1.toString(), row2.toString(), row3.toString());

        // First row
        for(int i = 0; i < 3; i++) {
            ItemStack item = openInventory.getItem(12 + i);
            if(item != null && !item.getType().isAir()) {
                shapedRecipe.setIngredient(chars[i], item.getType());
            }
        }

        // Second row
        for(int i = 0; i < 3; i++) {
            ItemStack item = openInventory.getItem(21 + i);
            if(item != null && !item.getType().isAir()) {
                shapedRecipe.setIngredient(chars[i + 3], item.getType());
            }
        }

        // Third row
        for(int i = 0; i < 3; i++) {
            ItemStack item = openInventory.getItem(30 + i);
            if(item != null && !item.getType().isAir()) {
                shapedRecipe.setIngredient(chars[i + 6], item.getType());
            }
        }

        robbingItem.updateShapedRecipe(shapedRecipe, plugin);

        String message = LightPlugin.getMessagesProvider().getMessage("general.recipe_updated");
        player.sendMessage(message);
    }
}
