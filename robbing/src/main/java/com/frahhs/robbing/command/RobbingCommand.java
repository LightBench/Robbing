package com.frahhs.robbing.command;

import com.frahhs.aikar.commands.BaseCommand;
import com.frahhs.aikar.commands.CommandHelp;
import com.frahhs.aikar.commands.annotation.*;
import com.frahhs.aikar.commands.bukkit.contexts.OnlinePlayer;
import com.frahhs.lightlib.LightPlugin;
import com.frahhs.lightlib.block.LightBlock;
import com.frahhs.lightlib.item.ItemManager;
import com.frahhs.lightlib.item.LightItem;
import com.frahhs.lightlib.provider.ConfigProvider;
import com.frahhs.lightlib.provider.MessagesProvider;
import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.feature.handcuffing.mcp.Handcuffing;
import com.frahhs.robbing.feature.handcuffing.mcp.HandcuffingController;
import com.frahhs.robbing.feature.safe.mcp.SafeController;
import com.frahhs.robbing.feature.safe.mcp.SafeModel;
import com.frahhs.robbing.feature.safe.mcp.SafePin;
import com.frahhs.robbing.menu.DashboardMenu;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.common.value.qual.IntRange;

@CommandAlias("robbing|rb")
@Description("Robbing main command")
public class RobbingCommand extends BaseCommand {
    private final LightPlugin plugin;
    MessagesProvider messagesProvider;
    ConfigProvider configProvider;

    public RobbingCommand(Robbing plugin) {
        this.plugin = plugin;
        messagesProvider = LightPlugin.getMessagesProvider();
        configProvider = LightPlugin.getConfigProvider();
    }

    @Default
    @CommandPermission("robbing.help")
    @Description("Show all the commands.")
    public void onRobbing(Player player, CommandHelp help) {
        help.showHelp();
    }

    @Subcommand("menu")
    @CommandPermission("git robbing.admin")
    @Description("Open a gui to change some settings.")
    public void onMenu(Player player) {
        DashboardMenu.open(player, plugin);
    }

    @Subcommand("handcuffs")
    @CommandPermission("robbing.admin")
    @CommandCompletion("* true|false")
    @Description("Toggle handcuffs on a Player.")
    public void onHandcuffs(CommandSender sender, OnlinePlayer player, boolean value) {
        if(!(sender instanceof Player)) {
            String message = messagesProvider.getMessage("commands.player_command");
            sender.sendMessage(message);
            return;
        }

        HandcuffingController controller = new HandcuffingController();
        if(value) {
            if(Handcuffing.isHandcuffed(player.getPlayer())) {
                String message = messagesProvider.getMessage("handcuffing.already_handcuffed");
                sender.sendMessage(message);
                return;
            }
            controller.putHandcuffs((Player) sender, player.getPlayer());
        } else {
            if(!Handcuffing.isHandcuffed(player.getPlayer())) {
                String message = messagesProvider.getMessage("handcuffing.not_handcuffed");
                sender.sendMessage(message);
                return;
            }
            controller.removeHandcuffs(player.getPlayer());
        }
    }

    @Subcommand("lock")
    @CommandPermission("robbing.lock")
    @Description("Lock the looking safe.")
    public void onLock(CommandSender sender, String pin) {
        if(!(sender instanceof Player))
            return;

        Block target = ((Player) sender).getTargetBlockExact(5);

        if(target == null)
            return;

        if(!LightBlock.isLightBlock(target)) {
            String message = messagesProvider.getMessage("safes.not_looking_safe");
            sender.sendMessage(message);
            return;
        }

        LightBlock safe = LightBlock.getFromLocation(target.getLocation());
        SafeController safeController = new SafeController();

        if(SafeModel.isLocked(safe)) {
            String message = messagesProvider.getMessage("safes.already_locked");
            sender.sendMessage(message);
            return;
        }

        if(!pin.matches("^[1-9]\\d{3}$")) {
            String message = messagesProvider.getMessage("safes.invalid_pin");
            sender.sendMessage(message);
            return;
        }

        // Limit safes feature
        boolean limitSafesEnabled = configProvider.getBoolean("safe.limit-locked-safes.enabled");
        int limitSafesAmount = configProvider.getInt("safe.limit-locked-safes.max-safes");
        if(limitSafesEnabled) {
            if(SafeModel.getByPlayer((Player) sender).size() >= limitSafesAmount) {
                String message = messagesProvider.getMessage("safes.limit_locking");
                sender.sendMessage(message);
                return;
            }
        }

        String message = messagesProvider.getMessage("safes.successfully_locked");
        sender.sendMessage(message);

        assert safe != null;
        safeController.lock(safe, pin, (Player) sender);
    }

    @Subcommand("unlock")
    @CommandPermission("robbing.unlock")
    @Description("Unock the looking safe.")
    public void onUnlock(CommandSender sender, String pin) {
        if(!(sender instanceof Player))
            return;

        Block target = ((Player) sender).getTargetBlockExact(5);

        if(target == null)
            return;

        if(!LightBlock.isLightBlock(target)) {
            String message = messagesProvider.getMessage("safes.not_looking_safe");
            sender.sendMessage(message);
            return;
        }

        LightBlock safe = LightBlock.getFromLocation(target.getLocation());

        SafeModel safeModel = SafeModel.getFromSafe(safe);
        SafeController safeController = new SafeController();

        if(!SafeModel.isLocked(safe)) {
            String message = messagesProvider.getMessage("safes.already_unlocked");
            sender.sendMessage(message);
            return;
        }

        if(!pin.matches("^[1-9]\\d{3}$")) {
            String message = messagesProvider.getMessage("safes.invalid_pin");
            sender.sendMessage(message);
            return;
        }

        if(safeModel.getPin().equals(new SafePin(pin))) {
            String message = messagesProvider.getMessage("safes.successfully_unlocked");
            sender.sendMessage(message);
            assert safe != null;
            safeController.unlock(safe);
        }
        else {
            String message = messagesProvider.getMessage("safes.wrong_pin");
            sender.sendMessage(message);
        }
    }

    @Subcommand("reload")
    @CommandPermission("robbing.reload")
    @Description("Reload the configuration of Robbing.")
    public void onReload(Player player) {
        plugin.onReload();
        String message = messagesProvider.getMessage("commands.reload");
        player.sendMessage(message);
    }

    @Subcommand("give")
    @CommandPermission("robbing.give")
    @CommandCompletion("* @RobbingItems 1|64")
    @Description("give a Robbing item to a player.")
    public void onGive(CommandSender sender, OnlinePlayer player, @Single String item_name, @IntRange(from=1, to=64) @Default("1") int amount) {
        ItemManager itemManager = LightPlugin.getItemsManager();

        String message;
        LightItem robbingItem;

        try {
            robbingItem = itemManager.get(item_name);
        } catch (IllegalArgumentException e) {
            robbingItem = null;
        }

        item_name = item_name.substring(0, 1).toUpperCase() + item_name.substring(1).toLowerCase();
        if(robbingItem == null || !robbingItem.isGivable()) {
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
