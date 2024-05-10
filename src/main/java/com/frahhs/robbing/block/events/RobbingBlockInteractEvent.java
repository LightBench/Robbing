package com.frahhs.robbing.block.events;

import com.frahhs.robbing.block.RobbingBlock;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an event that is called when a player interacts with a Robbing block for each hand. The hand can be determined using getHand().
 */
public class RobbingBlockInteractEvent extends RobbingBlockEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    protected Player player;
    protected ItemStack item;
    protected Action action;
    protected BlockFace blockFace;
    private Result useClickedBlock;
    private Result useItemInHand;
    private EquipmentSlot hand;
    private boolean cancel;

    public RobbingBlockInteractEvent(@NotNull final RobbingBlock clickedBlock, @NotNull final PlayerInteractEvent blockPlaceEvent) {
        super(clickedBlock);
        this.player = blockPlaceEvent.getPlayer();
        this.action = blockPlaceEvent.getAction();
        this.item = blockPlaceEvent.getItem();
        this.blockFace = blockPlaceEvent.getBlockFace();
        this.hand = blockPlaceEvent.getHand();
        cancel = false;

        useItemInHand = Result.DEFAULT;
        useClickedBlock = blockPlaceEvent.getClickedBlock() == null ? Result.DENY : Result.ALLOW;
    }

    /**
     * Returns the action type
     *
     * @return Action returns the type of interaction
     */
    @NotNull
    public Action getAction() {
        return action;
    }

    /**
     * Gets the cancellation state of this event.
     *
     * @return boolean cancellation state
     */
    @Override
    public boolean isCancelled() {
        return cancel;
    }

    /**
     * Sets the cancellation state of this event. A canceled event will not be
     * executed in the server, but will still pass to other plugins
     * <p>
     * Canceling this event will prevent use of food (player won't lose the
     * food item), prevent bows/snowballs/eggs from firing, etc. (player won't
     * lose the ammo)
     *
     * @param cancel true if you wish to cancel this event
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    /**
     * Returns the item in hand represented by this event
     *
     * @return ItemStack the item used
     */
    @Nullable
    public ItemStack getItem() {
        return this.item;
    }

    /**
     * Convenience method. Returns the material of the item represented by
     * this event
     *
     * @return Material the material of the item used
     */
    @NotNull
    public Material getMaterial() {
        if (!hasItem()) {
            return Material.AIR;
        }

        return item.getType();
    }

    /**
     * Check if this event involved an item
     *
     * @return boolean true if it did
     */
    public boolean hasItem() {
        return this.item != null;
    }

    /**
     * Convenience method to inform the user whether this was a block
     * placement event.
     *
     * @return boolean true if the item in hand was a block
     */
    public boolean isBlockInHand() {
        if (!hasItem()) {
            return false;
        }

        return item.getType().isBlock();
    }

    /**
     * Returns the face of the block that was clicked
     *
     * @return BlockFace returns the face of the block that was clicked
     */
    @NotNull
    public BlockFace getBlockFace() {
        return blockFace;
    }

    /**
     * This controls the action to take with the block (if any) that was
     * clicked on. This event gets processed for all blocks, but most don't
     * have a default action
     *
     * @return the action to take with the interacted block
     */
    @NotNull
    public Result useInteractedBlock() {
        return useClickedBlock;
    }

    /**
     * @param useInteractedBlock the action to take with the interacted block
     */
    public void setUseInteractedBlock(@NotNull Result useInteractedBlock) {
        this.useClickedBlock = useInteractedBlock;
    }

    /**
     * This controls the action to take with the item the player is holding.
     * This includes both blocks and items (such as flint and steel or
     * records). When this is set to default, it will be allowed if no action
     * is taken on the interacted block.
     *
     * @return the action to take with the item in hand
     */
    @NotNull
    public Result useItemInHand() {
        return useItemInHand;
    }

    /**
     * @param useItemInHand the action to take with the item in hand
     */
    public void setUseItemInHand(@NotNull Result useItemInHand) {
        this.useItemInHand = useItemInHand;
    }

    /**
     * The hand used to perform this interaction. May be null in the case of
     * {@link Action#PHYSICAL}.
     *
     * @return the hand used to interact. May be null.
     */
    @Nullable
    public EquipmentSlot getHand() {
        return hand;
    }

    public Player getPlayer() {
        return player;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
