package com.frahhs.robbing.block;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.feature.Provider;
import com.frahhs.robbing.item.ItemManager;
import com.frahhs.robbing.item.RobbingItem;
import com.frahhs.robbing.item.RobbingMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.scheduler.BukkitTask;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a block used in the robbing feature.
 */
public class RobbingBlock extends Provider {
    private final RobbingItem item;
    private ItemDisplay itemDisplay;
    private Location location;

    /**
     * Constructs a new RobbingBlock.
     *
     * @param item The robbing item associated with this block.
     * @param location The location of the block.
     */
    public RobbingBlock(RobbingItem item, Location location) {
        this.item = item;
        this.location = location;
    }

    /**
     * Sets the location of the block.
     *
     * @param location The new location of the block.
     */
    protected void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Retrieves the item display entity associated with this block.
     *
     * @return ItemDisplay entity.
     */
    protected ItemDisplay getItemDisplay() {
        return itemDisplay;
    }

    /**
     * Sets the item display entity associated with this block.
     *
     * @param itemDisplay The ItemDisplay.
     */
    protected void setItemDisplay(ItemDisplay itemDisplay) {
        this.itemDisplay = itemDisplay;
    }

    /**
     * Retrieves the robbing material of this block.
     *
     * @return The robbing material.
     */
    public RobbingMaterial getRobbingMaterial() {
        return item.getRobbingMaterial();
    }

    public Material getVanillaMaterial() {
        return item.getVanillaMaterial();
    }

    public PersistentDataContainer getPersistentDataContainer() {
        if(itemDisplay == null) {
            logger.error("Item Display (safe skin) not found, probably you issued killall command losing all the active safes.");
            throw new RuntimeException("Item Display not found.");
        }

        return itemDisplay.getPersistentDataContainer();
    }

    public UUID getUniqueId() {
        return itemDisplay.getUniqueId();
    }

    /**
     * Places the robbing block at the specified location.
     *
     * @param placer The player who is placing the block.
     */
    public void place(Player placer) {
        // Handle block facing
        double y = placer.getLocation().getYaw();
        float direction = 0;
        if (placer.getFacing().equals(BlockFace.SOUTH)) {
            direction = 0;
        }
        if (placer.getFacing().equals(BlockFace.WEST)) {
            direction = 90;
        }
        if (placer.getFacing().equals(BlockFace.NORTH)) {
            direction = 180;
        }
        if (placer.getFacing().equals(BlockFace.EAST)) {
            direction = 270;
        }

        location = location.getBlock().getLocation().add(0.5, 0.5, 0.5);
        location.setYaw(direction + 180);

        assert location.getWorld() != null;
        final ItemDisplay itemDisplay = location.getWorld().spawn(location, ItemDisplay.class);

        itemDisplay.setItemStack(item.getItemStack());
        itemDisplay.setBrightness(new Display.Brightness(15, 15));
        setItemDisplay(itemDisplay);


        save(placer);
    }

    /**
     * Destroys the robbing block at the specified location.
     */
    public void destroy() {
        RobbingBlock block = getFromLocation(location);

        if(block == null)
            return;

        if(block.getItemDisplay() != null)
            block.getItemDisplay().remove();

        block.getLocation().getBlock().setType(Material.AIR);

        remove();
    }

    /**
     * Retrieves the location of the block.
     *
     * @return The location of the block.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Saves the block's state to the database along with the player's details.
     *
     * @param placer The player who placed the block.
     */
    private void save(Player placer) {
        if (itemDisplay == null) {
            throw new RuntimeException("Tried to save a not placed Robbing block!");
        }

        try {
            PreparedStatement ps;
            ps = dbConnection.prepareStatement("INSERT INTO BlocksPlaced (placer, material, entityUUID, world, blockX, blockY, blockZ) VALUES (?, ?, ?, ?, ?, ?, ?);");
            ps.setString(1, placer.getUniqueId().toString());
            ps.setString(2, item.getRobbingMaterial().toString());
            ps.setString(3, itemDisplay.getUniqueId().toString());
            ps.setString(4, location.getWorld().getName());
            ps.setInt(5, location.getBlockX());
            ps.setInt(6, location.getBlockY());
            ps.setInt(7, location.getBlockZ());
            ps.executeUpdate();
            dbConnection.commit();
            ps.close();
        } catch (Exception e) {
            Robbing.getRobbingLogger().error("%s: %s", e.getClass().getName(), e.getMessage());
        }
    }

    /**
     * Removes the block's state from the database.
     */
    private void remove() {
        try {
            PreparedStatement ps;
            ps = dbConnection.prepareStatement("DELETE FROM BlocksPlaced WHERE world = ? AND blockX = ? AND blockY = ? AND blockZ = ?;");
            ps.setString(1, location.getWorld().getName());
            ps.setInt(2, location.getBlockX());
            ps.setInt(3, location.getBlockY());
            ps.setInt(4, location.getBlockZ());
            ps.executeUpdate();
            dbConnection.commit();
            ps.close();
        } catch (Exception e) {
            Robbing.getRobbingLogger().error("%s: %s", e.getClass().getName(), e.getMessage());
        }
    }

    /**
     * Checks if a block is a RobbingBlock.
     *
     * @param block The block to check.
     * @return True if the block is a RobbingBlock, false otherwise.
     */
    public static boolean isRobbingBlock(Block block) {
        Connection dbConnection = Robbing.getInstance().getRobbingDatabase().getConnection();

        try {
            PreparedStatement ps;
            ps = dbConnection.prepareStatement("SELECT * FROM BlocksPlaced WHERE world = ? AND blockX = ? AND blockY = ? AND blockZ = ?;");
            ps.setString(1, block.getLocation().getWorld().getName());
            ps.setInt(2, block.getLocation().getBlockX());
            ps.setInt(3, block.getLocation().getBlockY());
            ps.setInt(4, block.getLocation().getBlockZ());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return true;
            }
            ps.close();
        } catch (Exception e) {
            Robbing.getRobbingLogger().error("%s: %s", e.getClass().getName(), e.getMessage());
        }
        return false;
    }

    /**
     * Checks if a location corresponds to a RobbingBlock.
     *
     * @param location The location to check.
     * @return True if the location corresponds to a RobbingBlock, false otherwise.
     */
    public static boolean isRobbingBlock(Location location) {
        Connection dbConnection = Robbing.getInstance().getRobbingDatabase().getConnection();

        try {
            PreparedStatement ps;
            ps = dbConnection.prepareStatement("SELECT * FROM BlocksPlaced WHERE world = ? AND blockX = ? AND blockY = ? AND blockZ = ?;");
            ps.setString(1, location.getWorld().getName());
            ps.setInt(2, location.getBlockX());
            ps.setInt(3, location.getBlockY());
            ps.setInt(4, location.getBlockZ());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return true;
            }
            ps.close();
        } catch (Exception e) {
            Robbing.getRobbingLogger().error("%s: %s", e.getClass().getName(), e.getMessage());
        }
        return false;
    }

    /**
     * Checks if an item display corresponds to a RobbingBlock.
     *
     * @param entity The entity to check.
     * @return True if the item display corresponds to a RobbingBlock, false otherwise.
     */
    public static boolean isRobbingBlock(Entity entity) {
        Connection dbConnection = Robbing.getInstance().getRobbingDatabase().getConnection();

        try {
            PreparedStatement ps;
            ps = dbConnection.prepareStatement("SELECT * FROM BlocksPlaced WHERE entityUUID = ?;");
            ps.setString(1, entity.getUniqueId().toString());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return true;
            }
            ps.close();
        } catch (Exception e) {
            Robbing.getRobbingLogger().error("%s: %s", e.getClass().getName(), e.getMessage());
        }
        return false;
    }

    /**
     * Retrieves a RobbingBlock from a location.
     *
     * @param location The location to retrieve the block from.
     * @return The RobbingBlock at the specified location, or null if none is found.
     */
    public static RobbingBlock getFromLocation(Location location) {
        Connection dbConnection = Robbing.getInstance().getRobbingDatabase().getConnection();

        if (!isRobbingBlock(location)) {
            return null;
        }

        try {
            PreparedStatement ps;
            ps = dbConnection.prepareStatement("SELECT * FROM BlocksPlaced WHERE world = ? AND blockX = ? AND blockY = ? AND blockZ = ?;");
            ps.setString(1, location.getWorld().getName());
            ps.setInt(2, location.getBlockX());
            ps.setInt(3, location.getBlockY());
            ps.setInt(4, location.getBlockZ());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                RobbingMaterial material = RobbingMaterial.valueOf(rs.getString("material"));
                ItemManager itemManager = Robbing.getInstance().getItemsManager();
                RobbingItem item = itemManager.get(material);

                RobbingBlock block = new RobbingBlock(item, location);

                // Entity can be null if someone manually destroyed it
                String entityUUID = rs.getString("entityUUID");
                Entity itemDisplay = Bukkit.getEntity(UUID.fromString(entityUUID));
                if (itemDisplay != null) {
                    if (!itemDisplay.getType().equals(EntityType.ITEM_DISPLAY)) {
                        throw new RuntimeException("Database UUID is not an ItemDisplay UUID");
                    } else {
                        block.setItemDisplay((ItemDisplay) itemDisplay);
                    }
                }

                return block;
            }
            ps.close();
        } catch (Exception e) {
            Robbing.getRobbingLogger().error("%s: %s", e.getClass().getName(), e.getMessage());
        }
        return null;
    }

    /**
     * Retrieves a RobbingBlock from a UUID.
     *
     * @param entityUUID The UUID of the entity display.
     * @return The RobbingBlock associated with the entity display, or null if none is found.
     */
    public static RobbingBlock getFromUUID(UUID entityUUID) {
        Connection dbConnection = Robbing.getInstance().getRobbingDatabase().getConnection();

        try {
            PreparedStatement ps;
            ps = dbConnection.prepareStatement("SELECT * FROM BlocksPlaced WHERE entityUUID = ?;");
            ps.setString(1, entityUUID.toString());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                RobbingMaterial material = RobbingMaterial.valueOf(rs.getString("material"));
                ItemManager itemManager = Robbing.getInstance().getItemsManager();
                RobbingItem item = itemManager.get(material);

                World world = Bukkit.getWorld(rs.getString("world"));
                int blockX = rs.getInt("blockX");
                int blockY = rs.getInt("blockY");
                int blockZ = rs.getInt("blockZ");

                Location location = new Location(world, blockX, blockY, blockZ);
                RobbingBlock block = new RobbingBlock(item, location);

                // Entity can be null if someone manually destroyed it
                String itemDisplayUUID = rs.getString("entityUUID");
                Entity itemDisplay = Bukkit.getEntity(UUID.fromString(itemDisplayUUID));
                if (itemDisplay != null) {
                    if (!itemDisplay.getType().equals(EntityType.ITEM_DISPLAY)) {
                        throw new RuntimeException("Database UUID is not an item display UUID");
                    } else {
                        block.setItemDisplay((ItemDisplay) itemDisplay);
                    }
                }

                return block;
            }
            ps.close();
        } catch (Exception e) {
            Robbing.getRobbingLogger().error("%s: %s", e.getClass().getName(), e.getMessage());
        }
        return null;
    }
}
