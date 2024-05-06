package com.frahhs.robbing.features.handcuffing.listeners;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.features.handcuffing.models.HandcuffingModel;
import com.frahhs.robbing.managers.ConfigManager;
import com.frahhs.robbing.managers.MessagesManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Arrays;
import java.util.List;

public class HandcuffedListener implements Listener {
    private final ConfigManager configManager = Robbing.getInstance().getConfigManager();
    private final MessagesManager messagesManager = Robbing.getInstance().getMessagesManager();

    @EventHandler
    public void cannotIfHandcuffed(PlayerMoveEvent e) {
        Player p = e.getPlayer();

        // check if arriving position is null
        if(e.getTo() == null)
            return;
        // Check if player is handcuffed
        if(!HandcuffingModel.isHandcuffed(p))
            return;
        // Check if player moved himself
        if(e.getFrom().distance(e.getTo()) != 0)
            e.setCancelled(true);
    }

    @EventHandler
    public void cannotIfHandcuffed(PlayerInteractEntityEvent e) {
        Player p = e.getPlayer();

        if(HandcuffingModel.isHandcuffed(p)){
            e.setCancelled(true);
            String message = messagesManager.getMessage("handcuffing.you_cant_when_cuffed");
            p.sendMessage(message);
        }
    }

    @EventHandler
    public void cannotIfHandcuffed(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if(HandcuffingModel.isHandcuffed(p)){
            e.setCancelled(true);
            String message = messagesManager.getMessage("handcuffing.you_cant_when_cuffed");
            p.sendMessage(message);
        }
    }

    @EventHandler
    public void cannotIfHandcuffed(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();

        // If command is in permitted commands return
        List<String> curCommand = Arrays.asList(e.getMessage().substring(1).split(" "));
        for(String permittedCommand : configManager.getStringList("handcuffing.permitted_commands")) {
            boolean equals = true;
            List<String> curPermittedCommand = Arrays.asList(permittedCommand.split(" "));

            // Check if executed command args are less than permitted args param
            if(curCommand.size() < curPermittedCommand.size())
                equals = false;
            else
                // Check if first n args of executed command are different by curPermittedCommand args
                for(int i = 0; i < curPermittedCommand.size() ; i++)
                    if(!curCommand.get(i).equals(curPermittedCommand.get(i)))
                        equals = false;

            if(equals)
                return;
        }

        if(HandcuffingModel.isHandcuffed(p)) {
            e.setCancelled(true);
            String message = messagesManager.getMessage("handcuffing.you_cant_when_cuffed");
            p.sendMessage(message);
        }
    }

    @EventHandler
    public void cannotIfHandcuffed(BlockPlaceEvent e) {
        Player p = e.getPlayer();

        if(HandcuffingModel.isHandcuffed(p)) {
            e.setCancelled(true);
            String message = messagesManager.getMessage("handcuffing.you_cant_when_cuffed");
            p.sendMessage(message);
        }
    }

    @EventHandler
    public void cannotIfHandcuffed(BlockBreakEvent e) {
        Player p = e.getPlayer();

        if(HandcuffingModel.isHandcuffed(p)) {
            e.setCancelled(true);
            String message = messagesManager.getMessage("handcuffing.you_cant_when_cuffed");
            p.sendMessage(message);
        }
    }

    @EventHandler
    public void cannotIfHandcuffed(EntityDamageByEntityEvent e) {
        if(!(e.getDamager() instanceof Player))
            return;

        Player p = (Player) e.getDamager();

        if(HandcuffingModel.isHandcuffed(p)) {
            e.setCancelled(true);
            String message = messagesManager.getMessage("handcuffing.you_cant_when_cuffed");
            p.sendMessage(message);
        }
    }

    @EventHandler
    public void cannotIfHandcuffed(PlayerDropItemEvent e) {
        Player p = e.getPlayer();

        if(HandcuffingModel.isHandcuffed(p)) {
            e.setCancelled(true);
            String message = messagesManager.getMessage("handcuffing.you_cant_when_cuffed");
            p.sendMessage(message);
        }
    }

    @EventHandler
    public void cannotIfHandcuffed(EntityShootBowEvent e) {
        if(!(e.getEntity() instanceof Player))
            return;

        Player p = (Player) e.getEntity();

        if(HandcuffingModel.isHandcuffed(p)) {
            e.setCancelled(true);
            String message = messagesManager.getMessage("handcuffing.you_cant_when_cuffed");
            p.sendMessage(message);
        }
    }

    @EventHandler
    public void cannotIfHandcuffed(EntityToggleGlideEvent e) {
        if(!(e.getEntity() instanceof Player))
            return;

        Player p = (Player) e.getEntity();

        if(HandcuffingModel.isHandcuffed(p)) {
            // Block player gliding
            p.setGliding(false);
            e.setCancelled(true);
            String message = messagesManager.getMessage("handcuffing.you_cant_when_cuffed");
            p.sendMessage(message);
        }
    }
}
