package com.frahhs.robbing.commands;

import com.frahhs.robbing.Robbing;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class TestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player p = (Player) sender;

        if (args != null && args.length != 0) {
            if (Objects.equals(args[0], "reload")) {
                Robbing.getInstance().reload();
                p.sendMessage("Robbing reloaded!");
                return true;
            }
        }
        return false;
    }
}
