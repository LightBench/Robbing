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
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

/**
 * Represents a block used in the robbing feature.
 */
public class RobbingBlock extends Provider {
    private final RobbingItem item;
    private Entity armorStand;
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
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Retrieves the armor stand entity associated with this block.
     *
     * @return The armor stand entity.
     */
    public Entity getArmorStand() {
        return armorStand;
    }

    /**
     * Sets the armor stand entity associated with this block.
     *
     * @param armorStand The armor stand entity.
     */
    public void setArmorStand(Entity armorStand) {
        this.armorStand = armorStand;
    }

    /**
     * Retrieves the robbing material of this block.
     *
     * @return The robbing material.
     */
    public RobbingMaterial getRobbingMaterial() {
        return item.getRobbingMaterial();
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

        location = location.getBlock().getLocation().add(0.5, 0, 0.5);
        location.setYaw(direction + 180);

        Location spawn_location = location.clone();
        spawn_location.setY(-5);

        assert location.getWorld() != null;
        final ArmorStand armorStand = location.getWorld().spawn(spawn_location, ArmorStand.class);

        armorStand.setMarker(true);
        armorStand.setVisualFire(true);
        armorStand.setInvisible(true);

        assert armorStand.getEquipment() != null;
        armorStand.getEquipment().setHelmet(item.getItemStack());
        armorStand.teleport(location);
        setArmorStand(armorStand);

        // Teleport is not immediate, await before setting the block
        Location finalLocation = location.clone();

        save(placer);

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Robbing.getInstance(), new Runnable() {
            public void run() {
                // Run after waiting for things to settle
                finalLocation.getBlock().setType(Material.IRON_BLOCK);
            }
        }, 10);
    }

    /**
     * Destroys the robbing block at the specified location.
     */
    public void destroy() {
        RobbingBlock block = getFromLocation(location);

        if (block == null) {
            return;
        }

        if (block.getArmorStand() != null) {
            block.getArmorStand().remove();
        }

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
     * Saves the block's state to the database.
     */
    private void save() {
        if (armorStand == null) {
            throw new RuntimeException("Tried to save a not placed Robbing block!");
        }

        try {
            PreparedStatement ps;
            ps = dbConnection.prepareStatement("INSERT INTO BlocksPlaced (material, armorStandUUID, world, blockX, blockY, blockZ) VALUES (?, ?, ?, ?, ?, ?);");
            ps.setString(1, item.getRobbingMaterial().toString());
            ps.setString(2, armorStand.getUniqueId().toString());
            ps.setString(3, location.getWorld().getName());
            ps.setInt(4, location.getBlockX());
            ps.setInt(5, location.getBlockY());
            ps.setInt(6, location.getBlockZ());
            ps.executeUpdate();
            dbConnection.commit();
            ps.close();
        } catch (Exception e) {
            Robbing.getInstance().getRobbingLogger().error("%s: %s", e.getClass().getName(), e.getMessage());
        }
    }

    /**
     * Saves the block's state to the database along with the player's details.
     *
     * @param placer The player who placed the block.
     */
    private void save(Player placer) {
        if (armorStand == null) {
            throw new RuntimeException("Tried to save a not placed Robbing block!");
        }

        try {
            PreparedStatement ps;
            ps = dbConnection.prepareStatement("INSERT INTO BlocksPlaced (placer, material, armorStandUUID, world, blockX, blockY, blockZ) VALUES (?, ?, ?, ?, ?, ?, ?);");
            ps.setString(1, placer.getUniqueId().toString());
            ps.setString(2, item.getRobbingMaterial().toString());
            ps.setString(3, armorStand.getUniqueId().toString());
            ps.setString(4, location.getWorld().getName());
            ps.setInt(5, location.getBlockX());
            ps.setInt(6, location.getBlockY());
            ps.setInt(7, location.getBlockZ());
            ps.executeUpdate();
            dbConnection.commit();
            ps.close();
        } catch (Exception e) {
            Robbing.getInstance().getRobbingLogger().error("%s: %s", e.getClass().getName(), e.getMessage());
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
            Robbing.getInstance().getRobbingLogger().error("%s: %s", e.getClass().getName(), e.getMessage());
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
            Robbing.getInstance().getRobbingLogger().error("%s: %s", e.getClass().getName(), e.getMessage());
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
            Robbing.getInstance().getRobbingLogger().error("%s: %s", e.getClass().getName(), e.getMessage());
        }
        return false;
    }

    /**
     * Checks if an armor stand corresponds to a RobbingBlock.
     *
     * @param armorStand The armor stand to check.
     * @return True if the armor stand corresponds to a RobbingBlock, false otherwise.
     */
    public static boolean isRobbingBlock(Entity armorStand) {
        Connection dbConnection = Robbing.getInstance().getRobbingDatabase().getConnection();

        try {
            PreparedStatement ps;
            ps = dbConnection.prepareStatement("SELECT * FROM BlocksPlaced WHERE armorStandUUID = ?;");
            ps.setString(1, armorStand.getUniqueId().toString());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return true;
            }
            ps.close();
        } catch (Exception e) {
            Robbing.getInstance().getRobbingLogger().error("%s: %s", e.getClass().getName(), e.getMessage());
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

                // Armor stand can be null if someone manually destroyed it
                String armorStandId = rs.getString("armorStandUUID");
                Entity armorStand = Bukkit.getEntity(UUID.fromString(armorStandId));
                if (armorStand != null) {
                    if (!armorStand.getType().equals(EntityType.ARMOR_STAND)) {
                        throw new RuntimeException("Database UUID is not an armor stand UUID");
                    } else {
                        block.setArmorStand(armorStand);
                    }
                }

                return block;
            }
            ps.close();
        } catch (Exception e) {
            Robbing.getInstance().getRobbingLogger().error("%s: %s", e.getClass().getName(), e.getMessage());
        }
        return null;
    }

    /**
     * Retrieves a RobbingBlock from an armor stand UUID.
     *
     * @param entityUUID The UUID of the armor stand.
     * @return The RobbingBlock associated with the armor stand, or null if none is found.
     */
    public static RobbingBlock getFromArmorStandUUID(UUID entityUUID) {
        Connection dbConnection = Robbing.getInstance().getRobbingDatabase().getConnection();

        try {
            PreparedStatement ps;
            ps = dbConnection.prepareStatement("SELECT * FROM BlocksPlaced WHERE armorStandUUID = ?;");
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

                // Armor stand can be null if someone manually destroyed it
                String armorStandId = rs.getString("armorStandUUID");
                Entity armorStand = Bukkit.getEntity(UUID.fromString(armorStandId));
                if (armorStand != null) {
                    if (!armorStand.getType().equals(EntityType.ARMOR_STAND)) {
                        throw new RuntimeException("Database UUID is not an armor stand UUID");
                    } else {
                        block.setArmorStand(armorStand);
                    }
                }

                return block;
            }
            ps.close();
        } catch (Exception e) {
            Robbing.getInstance().getRobbingLogger().error("%s: %s", e.getClass().getName(), e.getMessage());
        }
        return null;
    }
}
