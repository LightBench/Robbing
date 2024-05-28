package com.frahhs.robbing.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.block.RobbingBlock;
import com.frahhs.robbing.feature.safe.mcp.SafePin;
import com.frahhs.robbing.feature.safe.mcp.SafeController;
import com.frahhs.robbing.feature.safe.mcp.SafeModel;
import com.frahhs.robbing.item.ItemManager;
import com.frahhs.robbing.item.RobbingItem;
import com.frahhs.robbing.item.RobbingMaterial;
import com.frahhs.robbing.provider.MessagesProvider;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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

    @Subcommand("lock")
    @CommandPermission("robbing.lock")
    public void onLock(CommandSender sender, String pin) {
        if(!(sender instanceof Player))
            return;

        Block target = ((Player) sender).getTargetBlockExact(5);

        if(target == null)
            return;

        if(!RobbingBlock.isRobbingBlock(target)) {
            //TODO: add to messages
            sender.sendMessage("You have to look a safe to use this command");
            return;
        }

        RobbingBlock safe = RobbingBlock.getFromLocation(target.getLocation());
        SafeController safeController = new SafeController();

        if(SafeModel.isLocked(safe)) {
            //TODO: add to messages
            sender.sendMessage("Safe already locked");
            return;
        }

        if(!pin.matches("^[1-9]\\d{3}$")) {
            //TODO: add to messages
            sender.sendMessage("Invalid pin, the pin must be a 4 length digits");
            return;
        }

        //TODO: add to messages
        sender.sendMessage("Safe successfully locked");
        safeController.lock(safe, pin);
    }

    @Subcommand("unlock")
    @CommandPermission("robbing.unlock")
    public void onUnlock(CommandSender sender, String pin) {
        if(!(sender instanceof Player))
            return;

        Block target = ((Player) sender).getTargetBlockExact(5);

        if(target == null)
            return;

        if(!RobbingBlock.isRobbingBlock(target)) {
            //TODO: add to messages
            sender.sendMessage("You have to look a safe to use this command");
            return;
        }

        RobbingBlock safe = RobbingBlock.getFromLocation(target.getLocation());

        SafeModel safeModel = SafeModel.getFromSafe(safe);
        SafeController safeController = new SafeController();

        if(!SafeModel.isLocked(safe)) {
            //TODO: add to messages
            sender.sendMessage("Safe already unlocked");
            return;
        }

        if(!pin.matches("^[1-9]\\d{3}$")) {
            //TODO: add to messages
            sender.sendMessage("Invalid pin, the pin must be a 4 length digits");
            return;
        }

        if(safeModel.getPin().equals(new SafePin(pin))) {
            //TODO: add to messages
            sender.sendMessage("Safe successfully unlocked");
            safeController.unlock(safe);
        }
        else {
            //TODO: add to messages
            sender.sendMessage("Wrong pin");
        }
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
