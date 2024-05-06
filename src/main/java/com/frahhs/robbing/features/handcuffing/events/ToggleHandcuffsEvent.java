package com.frahhs.robbing.features.handcuffing.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ToggleHandcuffsEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean isCancelled;

    private final Player handcuffed;
    private final Player whoHandcuffed;
    private final boolean isPuttingOn;


    public ToggleHandcuffsEvent(Player handcuffed, Player whoHandcuffed, boolean isPuttingOn) {
        this.handcuffed = handcuffed;
        this.whoHandcuffed = whoHandcuffed;
        this.isPuttingOn = isPuttingOn;
    }

    public Player getHandcuffed() {
        return this.handcuffed;
    }

    public Player getWhoHandcuffed() {
        return this.whoHandcuffed;
    }

    public boolean isPuttingOn() { return this.isPuttingOn; }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() { return HANDLERS; }
}
