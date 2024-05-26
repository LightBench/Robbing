package com.frahhs.robbing.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.item.ItemManager;
import com.frahhs.robbing.item.RobbingItem;
import com.frahhs.robbing.item.RobbingMaterial;
import com.frahhs.robbing.provider.MessagesProvider;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.common.value.qual.IntRange;

@CommandAlias("robbing|rb")
@Description("Some ACF Command")
public class RobbingCommand extends BaseCommand {
    private Robbing plugin;
    MessagesProvider messagesProvider;

    public RobbingCommand(Robbing plugin) {
        this.plugin = plugin;
        messagesProvider = plugin.getMessagesProvider();
    }

    @Default
    @CommandPermission("robbing.help")
    public void onRobbing(Player player, CommandHelp help) {
        help.showHelp();
    }

    @Subcommand("gui")
    public void onGui(Player player) {
        // https://www.spigotmc.org/threads/negative-space-font-resource-pack.440952/
        //Inventory inventory = Bukkit.createInventory(null, 6*9, "\uF001§f\uD83D\uDE97");
        //SafeUnlockGUI gui = new SafeUnlockGUI();
        //player.openInventory(gui.getInventory());
    }

    @Subcommand("guis")
    public void onGuis(CommandSender sender, String title) {
        // https://www.spigotmc.org/threads/negative-space-font-resource-pack.440952/
        Inventory inventory = Bukkit.createInventory(null, 6*9, title);
        ((Player)sender).openInventory(inventory);
    }

    @Subcommand("reload")
    @CommandPermission("robbing.reload")
    @Description("Reload the configuration of Robbing.")
    public void onReload(Player player) {
        plugin.reload();
        String message = messagesProvider.getMessage("commands.reload");
        player.sendMessage(message);
    }

    @Subcommand("give")
    @CommandPermission("robbing.give")
    @CommandCompletion("* @RobbingItems 1|64")
    public void onGive(CommandSender sender, OnlinePlayer player, @Single String item_name, @IntRange(from=1, to=64) @Default("1") int amount) {
        ItemManager itemManager = plugin.getItemsManager();

        String message;
        RobbingItem robbingItem;

        robbingItem = itemManager.get(RobbingMaterial.matchMaterial(item_name));

        item_name = item_name.substring(0, 1).toUpperCase() + item_name.substring(1).toLowerCase();
        if(robbingItem == null) {
            message = messagesProvider.getMessage("commands.item_not_found");
            message = message.replace("{item}", item_name);
            sender.sendMessage(message);
            return;
        }

        ItemStack item = robbingItem.getItemStack();
        item.setAmount(amount);
        player.getPlayer().getInventory().addItem(item);

        message = messagesProvider.getMessage("commands.given");
        message = message.replace("{player}", player.getPlayer().getDisplayName());
        message = message.replace("{item}", item_name);
        message = message.replace("{amount}", Integer.toString(amount));
        sender.sendMessage(message);
    }

    @CatchUnknown
    public void onUnknown(CommandSender sender) {
        String message = messagesProvider.getMessage("commands.unknown");
        sender.sendMessage(message);
    }

    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }
}
