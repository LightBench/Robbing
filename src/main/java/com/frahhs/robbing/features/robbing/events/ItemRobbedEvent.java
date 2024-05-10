package com.frahhs.robbing.features.robbing.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an event indicating that an item has been stolen from a player's inventory.
 */
public class ItemRobbedEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel;
    protected final ItemStack robbedItem;
    protected final Player robber;
    protected final Player robbed;

    /**
     * Constructs a new ItemRobbedEvent.
     *
     * @param robbedItem The item that was stolen.
     * @param robber     The player who stole the item.
     * @param robbed     The player from whom the item was stolen.
     */
    public ItemRobbedEvent(@NotNull final ItemStack robbedItem, @NotNull final Player robber, @NotNull final Player robbed) {
        this.robbedItem = robbedItem;
        this.robber = robber;
        this.robbed = robbed;
        cancel = false;
    }

    @Override
    public boolean isCancelled() {
        return this.cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    /**
     * Gets the item that was stolen.
     *
     * @return The stolen item.
     */
    public ItemStack getRobbedItem() {
        return this.robbedItem;
    }

    /**
     * Gets the player who stole the item.
     *
     * @return The player who stole the item.
     */
    public Player getRobber() {
        return this.robber;
    }

    /**
     * Gets the player from whom the item was stolen.
     *
     * @return The player from whom the item was stolen.
     */
    public Player getRobbed() {
        return this.robbed;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
