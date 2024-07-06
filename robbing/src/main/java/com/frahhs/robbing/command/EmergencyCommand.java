package com.frahhs.robbing.command;

import com.frahhs.aikar.commands.BaseCommand;
import com.frahhs.aikar.commands.CommandHelp;
import com.frahhs.aikar.commands.annotation.*;
import com.frahhs.lightlib.LightPlugin;
import com.frahhs.lightlib.provider.ConfigProvider;
import com.frahhs.lightlib.provider.MessagesProvider;
import com.frahhs.lightlib.util.Cooldown;
import com.frahhs.robbing.Robbing;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@CommandAlias("911")
@Description("Emergency call command")
public class EmergencyCommand extends BaseCommand {
    private final LightPlugin plugin;
    MessagesProvider messagesProvider;
    ConfigProvider configProvider;

    private static Map<Player, Cooldown> cooldown;

    public EmergencyCommand(Robbing plugin) {
        this.plugin = plugin;
        messagesProvider = LightPlugin.getMessagesProvider();
        configProvider = LightPlugin.getConfigProvider();

        cooldown = new HashMap<>();
    }

    @Default
    @CommandPermission("robbing.911")
    @Description("Send al alert to the police.")
    public void on911(Player player, String reason) {
        if(!configProvider.getBoolean("emergencycall.enabled")) {
            player.sendMessage(messagesProvider.getMessage("general.disabled_feature"));
            return;
        }

        if(cooldown.containsKey(player)) {
            if (cooldown.get(player).getResidualTime() > 0) {
                player.sendMessage(messagesProvider.getMessage("general.cooldown").replace("{time}", Long.toString(cooldown.get(player).getResidualTime())));
                return;
            } else {
                cooldown.remove(player);
            }
        }

        for(Player curPlayer : Bukkit.getOnlinePlayers()) {
            if(curPlayer.hasPermission("robbing.police")) {
                String location = String.format("(%.2f, %.2f, %.2f)", player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
                String message = messagesProvider.getMessage("emergency.alert").replace("{player}", player.getDisplayName()).replace("{reason}", reason).replace("{coordinates}", location);
                curPlayer.sendMessage(message);
            }
        }
        String message = messagesProvider.getMessage("emergency.alert_sent");
        player.sendMessage(message);

        cooldown.put(player, new Cooldown(System.currentTimeMillis(), configProvider.getInt("emergencycall.cooldown")));
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
