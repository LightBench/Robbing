package com.frahhs.robbing.features.rob.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * Represents an event indicating that an item has been stolen from a player's inventory.
 */
public class ItemRobbedEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private boolean isCancelled;

    private final ItemStack robbedItem;
    private final Player robber;
    private final Player robbed;

    /**
     * Constructs a new ItemRobbedEvent.
     *
     * @param robbedItem The item that was stolen.
     * @param robber     The player who stole the item.
     * @param robbed     The player from whom the item was stolen.
     */
    public ItemRobbedEvent(ItemStack robbedItem, Player robber, Player robbed) {
        this.robbedItem = robbedItem;
        this.robber = robber;
        this.robbed = robbed;
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

    /**
     * Gets the handler list for this event.
     *
     * @return The handler list for this event.
     */
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
