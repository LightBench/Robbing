package com.frahhs.robbing.menu;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.item.ItemManager;
import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.GuiPageElement;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class DashboardMenu {
    public static void open(Player player, JavaPlugin plugin) {
        ItemManager itemManager = Robbing.getInstance().getItemsManager();
        String[] guiSetup = {
                "         ",
                "    g    ",
                "b fpdnl  "
        };

        InventoryGui gui = new InventoryGui(plugin, null, "Menu (Page %page% of %pages%)", guiSetup);
        gui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE)); // fill the empty slots with this

        GuiElementGroup group = new GuiElementGroup('g');
        group.addElement(new StaticGuiElement('g',
                new ItemStack(Material.PAPER),
                click -> {
                    player.closeInventory();
                    RecipeMenu.open(player, plugin);
                    return true; // returning true will cancel the click event and stop taking the item
                },
                ChatColor.WHITE + "Custom items recipe"
        ));
        gui.addElement(group);

        // First page
        gui.addElement(new GuiPageElement('f', new ItemStack(Material.ARROW), GuiPageElement.PageAction.FIRST, "Go to first page (current: %page%)"));

        // Previous page
        gui.addElement(new GuiPageElement('p', new ItemStack(Material.OAK_SIGN), GuiPageElement.PageAction.PREVIOUS, "Go to previous page (%prevpage%)"));

        // Next page
        gui.addElement(new GuiPageElement('n', new ItemStack(Material.OAK_SIGN), GuiPageElement.PageAction.NEXT, "Go to next page (%nextpage%)"));

        // Last page
        gui.addElement(new GuiPageElement('l', new ItemStack(Material.ARROW), GuiPageElement.PageAction.LAST, "Go to last page (%pages%)"));

        gui.show(player);
    }
}
