package com.frahhs.robbing.features.block.events;

import com.frahhs.robbing.item.RobbingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.jetbrains.annotations.NotNull;

public class RobbingBlockBreakEvent extends RobbingBlockEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel;
    protected Player player;
    protected boolean dropItems;
    protected int exp;

    public RobbingBlockBreakEvent(@NotNull final RobbingBlock placedBlock, @NotNull final BlockBreakEvent blockBreakEvent) {
        super(placedBlock);
        this.player = blockBreakEvent.getPlayer();
        this.dropItems = blockBreakEvent.isDropItems();
        this.exp = blockBreakEvent.getExpToDrop();
        cancel = false;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    /**
     * Gets the Player that is breaking the block involved in this event.
     *
     * @return The Player that is breaking the block involved in this event
     */
    @NotNull
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets whether the block will attempt to drop items as it normally
     * would.
     * <p>
     * If and only if this is false then {@link BlockDropItemEvent} will not be
     * called after this event.
     *
     * @param dropItems Whether the block will attempt to drop items
     */
    public void setDropItems(boolean dropItems) {
        this.dropItems = dropItems;
    }

    /**
     * Gets whether the block will attempt to drop items.
     * <p>
     * If and only if this is false then {@link BlockDropItemEvent} will not be
     * called after this event.
     *
     * @return Whether the block will attempt to drop items
     */
    public boolean isDropItems() {
        return this.dropItems;
    }

    /**
     * Get the experience dropped by the block after the event has processed
     *
     * @return The experience to drop
     */
    public int getExpToDrop() {
        return exp;
    }

    /**
     * Set the amount of experience dropped by the block after the event has
     * processed
     *
     * @param exp 1 or higher to drop experience, else nothing will drop
     */
    public void setExpToDrop(int exp) {
        this.exp = exp;
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
