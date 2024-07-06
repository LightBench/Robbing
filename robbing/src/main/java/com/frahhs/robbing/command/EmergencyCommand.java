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
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.common.value.qual.IntRange;

@CommandAlias("911")
@Description("Emergency call command")
public class EmergencyCommand extends BaseCommand {
    private final LightPlugin plugin;
    MessagesProvider messagesProvider;
    ConfigProvider configProvider;

    public EmergencyCommand(Robbing plugin) {
        this.plugin = plugin;
        messagesProvider = LightPlugin.getMessagesProvider();
        configProvider = LightPlugin.getConfigProvider();
    }

    @Default
    @CommandPermission("robbing.911")
    @Description("Send al alert to the police.")
    public void on911(Player player, String reason) {
        for(Player curPlayer : Bukkit.getOnlinePlayers()) {
            if(curPlayer.hasPermission("robbing.police")) {
                String message = messagesProvider.getMessage("emergency.alert").replace("{player}", player.getDisplayName()).replace("{reason}", reason);
                curPlayer.sendMessage(message);
            }
        }
        String message = messagesProvider.getMessage("emergency.alert_sent");
        player.sendMessage(message);
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
