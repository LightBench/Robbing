package com.frahhs.robbing.feature.handcuffing.listener;

import com.frahhs.robbing.RBListener;
import com.frahhs.robbing.feature.handcuffing.model.Handcuffing;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
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

public class HandcuffedListener extends RBListener {
    @EventHandler
    public void cannotIfHandcuffed(PlayerMoveEvent e) {
        Player player = e.getPlayer();

        // check if arriving position is null
        if(e.getTo() == null)
            return;
        // Check if player is handcuffed
        if(!Handcuffing.isHandcuffed(player))
            return;
        // Check if player moved himself
        if(e.getFrom().distance(e.getTo()) != 0)
            e.setCancelled(true);
    }

    @EventHandler
    public void cannotIfHandcuffed(PlayerInteractEntityEvent e) {
        Player player = e.getPlayer();

        if(Handcuffing.isHandcuffed(player)){
            e.setCancelled(true);
            String message = messages.getMessage("handcuffing.you_cant_when_cuffed");
            player.sendMessage(message);
        }
    }

    @EventHandler
    public void cannotIfHandcuffed(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        if(Handcuffing.isHandcuffed(player)){
            e.setCancelled(true);
            String message = messages.getMessage("handcuffing.you_cant_when_cuffed");
            player.sendMessage(message);
        }
    }

    @EventHandler
    public void cannotIfHandcuffed(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();

        // If command is in permitted commands return
        List<String> curCommand = Arrays.asList(e.getMessage().substring(1).split(" "));
        for(String permittedCommand : config.getStringList("handcuffing.permitted_commands")) {
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

        if(Handcuffing.isHandcuffed(player)) {
            e.setCancelled(true);
            String message = messages.getMessage("handcuffing.you_cant_when_cuffed");
            player.sendMessage(message);
        }
    }

    @EventHandler
    public void cannotIfHandcuffed(BlockPlaceEvent e) {
        Player player = e.getPlayer();

        if(Handcuffing.isHandcuffed(player)) {
            e.setCancelled(true);
            String message = messages.getMessage("handcuffing.you_cant_when_cuffed");
            player.sendMessage(message);
        }
    }

    @EventHandler
    public void cannotIfHandcuffed(BlockBreakEvent e) {
        Player player = e.getPlayer();

        if(Handcuffing.isHandcuffed(player)) {
            e.setCancelled(true);
            String message = messages.getMessage("handcuffing.you_cant_when_cuffed");
            player.sendMessage(message);
        }
    }

    @EventHandler
    public void cannotIfHandcuffed(EntityDamageByEntityEvent e) {
        if(!(e.getDamager() instanceof Player))
            return;

        Player player = (Player) e.getDamager();

        if(Handcuffing.isHandcuffed(player)) {
            e.setCancelled(true);
            String message = messages.getMessage("handcuffing.you_cant_when_cuffed");
            player.sendMessage(message);
        }
    }

    @EventHandler
    public void cannotIfHandcuffed(PlayerDropItemEvent e) {
        Player player = e.getPlayer();

        if(Handcuffing.isHandcuffed(player)) {
            e.setCancelled(true);
            String message = messages.getMessage("handcuffing.you_cant_when_cuffed");
            player.sendMessage(message);
        }
    }

    @EventHandler
    public void cannotIfHandcuffed(EntityShootBowEvent e) {
        if(!(e.getEntity() instanceof Player))
            return;

        Player player = (Player) e.getEntity();

        if(Handcuffing.isHandcuffed(player)) {
            e.setCancelled(true);
            String message = messages.getMessage("handcuffing.you_cant_when_cuffed");
            player.sendMessage(message);
        }
    }

    @EventHandler
    public void cannotIfHandcuffed(EntityToggleGlideEvent e) {
        if(!(e.getEntity() instanceof Player))
            return;

        Player player = (Player) e.getEntity();

        if(Handcuffing.isHandcuffed(player)) {
            // Block player gliding
            player.setGliding(false);
            e.setCancelled(true);
            String message = messages.getMessage("handcuffing.you_cant_when_cuffed");
            player.sendMessage(message);
        }
    }
}
