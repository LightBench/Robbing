package com.frahhs.robbing.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.item.ItemsManager;
import com.frahhs.robbing.item.RobbingMaterial;
import com.frahhs.robbing.providers.MessagesProvider;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.common.value.qual.IntRange;

@CommandAlias("robbing|rb")
@Description("Some ACF Command")
public class RobbingCommand extends BaseCommand {
    MessagesProvider messagesProvider;

    public RobbingCommand() {
        messagesProvider = Robbing.getInstance().getMessagesProvider();
    }

    @Default
    @CommandPermission("robbing.help")
    public void onRobbing(Player player, CommandHelp help) {
        help.showHelp();
    }

    @Subcommand("reload")
    @CommandPermission("robbing.reload")
    @Description("Reload the configuration of Robbing.")
    public void onReload(Player player) {
        Robbing.getInstance().reload();
        String message = messagesProvider.getMessage("commands.reload");
        player.sendMessage(message);
    }

    @Subcommand("give")
    @CommandPermission("robbing.give")
    @CommandCompletion("* @RBItems 1|64")
    public void onGive(CommandSender sender, OnlinePlayer player, @Single String item_name, @IntRange(from=1, to=64) @Default("1") int amount) {
        ItemsManager itemsManager = Robbing.getInstance().getItemsManager();

        String message;
        ItemStack item = null;

        item = itemsManager.getByName(item_name).getItemStack();

        item_name = item_name.substring(0, 1).toUpperCase() + item_name.substring(1).toLowerCase();
        if(item == null) {
            message = messagesProvider.getMessage("commands.item_not_found");
            message = message.replace("{item}", item_name);
            sender.sendMessage(message);
            return;
        }

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
