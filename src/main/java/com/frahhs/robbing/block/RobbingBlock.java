package com.frahhs.robbing.block;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.features.BaseModel;
import com.frahhs.robbing.item.ItemsManager;
import com.frahhs.robbing.item.RobbingItem;
import com.frahhs.robbing.item.RobbingMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

public class RobbingBlock extends BaseModel {
    private RobbingItem item;
    private Entity armorStand;
    private Location location;

    private final Connection dbConnection;

    public RobbingBlock(RobbingItem item, Location location) {
        this.item = item;
        this.armorStand = getArmorStand();
        this.location = location;

        dbConnection = Robbing.getInstance().getRBDatabase().getConnection();
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Entity getArmorStand() {
        return armorStand;
    }

    public void setArmorStand(Entity armorStand) {
        this.armorStand = armorStand;
    }

    public RobbingMaterial getRobbingMaterial() {
        return item.getRBMaterial();
    }

    public void place() {
        location = location.getBlock().getLocation().add(0.5, 0, 0.5);

        Location spawn_location = location.clone();
        spawn_location.setY(-5);

        assert location.getWorld() != null;
        final ArmorStand armorStand = location.getWorld().spawn(spawn_location, ArmorStand.class);

        armorStand.setMarker(true);
        armorStand.setVisualFire(true);
        armorStand.setInvisible(true);
        armorStand.getEquipment().setHelmet(item.getItemStack());
        armorStand.teleport(location);
        setArmorStand(armorStand);

        // Teleport is not immediately, await before set the block
        Location finalLocation = location.clone();
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Robbing.getInstance(), new Runnable(){
            public void run(){
                //Run after wait things here
                finalLocation.getBlock().setType(Material.IRON_BLOCK);
            }
        }, 12);

        save();
    }

    public void destroy() {
        RobbingBlock block = getFromLocation(location);

        if(block == null)
            return;

        if(block.getArmorStand() != null)
            block.getArmorStand().remove();

        block.getLocation().getBlock().setType(Material.AIR);

        remove();
    }

    public Location getLocation() {
        return location;
    }

    private void save() {
        if(armorStand == null)
            throw new RuntimeException("Tried to save a not placed Robbing block!");

        try {
            PreparedStatement ps;
            ps = dbConnection.prepareStatement("INSERT INTO BlocksPlaced (itemName, armorStandUUID, world, blockX, blockY, blockZ) VALUES (?, ?, ?, ?, ?, ?);");
            ps.setString(1, item.getItemName());
            ps.setString(2, armorStand.getUniqueId().toString());
            ps.setString(3, location.getWorld().getName());
            ps.setInt(4, location.getBlockX());
            ps.setInt(5, location.getBlockY());
            ps.setInt(6, location.getBlockZ());
            ps.executeUpdate();
            dbConnection.commit();
            ps.close();
        } catch ( Exception e ) {
            Robbing.getInstance().getRBLogger().error("%s: %s",e.getClass().getName(), e.getMessage());
        }
    }

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
        } catch ( Exception e ) {
            Robbing.getInstance().getRBLogger().error("%s: %s",e.getClass().getName(), e.getMessage());
        }
    }

    public static boolean isRBBlock(Block block) {
        Connection dbConnection = Robbing.getInstance().getRBDatabase().getConnection();

        try {
            PreparedStatement ps;
            ps = dbConnection.prepareStatement("SELECT * FROM BlocksPlaced WHERE world = ? AND blockX = ? AND blockY = ? AND blockZ = ?;");
            ps.setString(1, block.getLocation().getWorld().getName());
            ps.setInt(2, block.getLocation().getBlockX());
            ps.setInt(3, block.getLocation().getBlockY());
            ps.setInt(4, block.getLocation().getBlockZ());
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                return true;
            }
            ps.close();
        } catch ( Exception e ) {
            Robbing.getInstance().getRBLogger().error("%s: %s",e.getClass().getName(), e.getMessage());
        }
        return false;
    }

    public static boolean isRBBlock(Location location) {
        Connection dbConnection = Robbing.getInstance().getRBDatabase().getConnection();

        try {
            PreparedStatement ps;
            ps = dbConnection.prepareStatement("SELECT * FROM BlocksPlaced WHERE world = ? AND blockX = ? AND blockY = ? AND blockZ = ?;");
            ps.setString(1, location.getWorld().getName());
            ps.setInt(2, location.getBlockX());
            ps.setInt(3, location.getBlockY());
            ps.setInt(4, location.getBlockZ());
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                return true;
            }
            ps.close();
        } catch ( Exception e ) {
            Robbing.getInstance().getRBLogger().error("%s: %s",e.getClass().getName(), e.getMessage());
        }
        return false;
    }

    public static boolean isRBBlock(Entity armorStand) {
        Connection dbConnection = Robbing.getInstance().getRBDatabase().getConnection();

        try {
            PreparedStatement ps;
            ps = dbConnection.prepareStatement("SELECT * FROM BlocksPlaced WHERE armorStandUUID = ?;");
            ps.setString(1, armorStand.getUniqueId().toString());
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                return true;
            }
            ps.close();
        } catch ( Exception e ) {
            Robbing.getInstance().getRBLogger().error("%s: %s",e.getClass().getName(), e.getMessage());
        }
        return false;
    }

    public static RobbingBlock getFromLocation(Location location) {
        Connection dbConnection = Robbing.getInstance().getRBDatabase().getConnection();

        if(!isRBBlock(location))
            return null;

        try {
            PreparedStatement ps;
            ps = dbConnection.prepareStatement("SELECT * FROM BlocksPlaced WHERE world = ? AND blockX = ? AND blockY = ? AND blockZ = ?;");
            ps.setString(1, location.getWorld().getName());
            ps.setInt(2, location.getBlockX());
            ps.setInt(3, location.getBlockY());
            ps.setInt(4, location.getBlockZ());
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                String itemName = rs.getString("itemName");
                ItemsManager itemsManager = Robbing.getInstance().getItemsManager();
                RobbingItem item = itemsManager.getByName(itemName);

                RobbingBlock block = new RobbingBlock(item, location);

                // armor stand can be null if someone manually destroyed that
                String armorStandId = rs.getString("armorStandUUID");
                Entity armorStand = Bukkit.getEntity(UUID.fromString(armorStandId));
                if(armorStand != null) {
                    if(!armorStand.getType().equals(EntityType.ARMOR_STAND)) {
                        throw new RuntimeException("Database uuid is not an armor stand uuid");
                    } else {
                        block.setArmorStand(armorStand);
                    }
                }

                return block;
            }
            ps.close();
        } catch ( Exception e ) {
            Robbing.getInstance().getRBLogger().error("%s: %s",e.getClass().getName(), e.getMessage());
        }
        return null;
    }

    public static RobbingBlock getFromArmorStandUUID(UUID entityUUID) {
        Connection dbConnection = Robbing.getInstance().getRBDatabase().getConnection();

        try {
            PreparedStatement ps;
            ps = dbConnection.prepareStatement("SELECT * FROM BlocksPlaced WHERE armorStandUUID = ?;");
            ps.setString(1, entityUUID.toString());
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                String itemName = rs.getString("itemName");
                ItemsManager itemsManager = Robbing.getInstance().getItemsManager();
                RobbingItem item = itemsManager.getByName(itemName);

                World world = Bukkit.getWorld(rs.getString("world"));
                int blockX = rs.getInt("blockX");
                int blockY = rs.getInt("blockY");
                int blockZ = rs.getInt("blockZ");

                Location location = new Location(world, blockX, blockY, blockZ);
                RobbingBlock block = new RobbingBlock(item, location);

                // armor stand can be null if someone manually destroyed that
                String armorStandId = rs.getString("armorStandUUID");
                Entity armorStand = Bukkit.getEntity(UUID.fromString(armorStandId));
                if(armorStand != null) {
                    if(!armorStand.getType().equals(EntityType.ARMOR_STAND)) {
                        throw new RuntimeException("Database uuid is not an armor stand uuid");
                    } else {
                        block.setArmorStand(armorStand);
                    }
                }

                return block;
            }
            ps.close();
        } catch ( Exception e ) {
            Robbing.getInstance().getRBLogger().error("%s: %s",e.getClass().getName(), e.getMessage());
        }
        return null;
    }
}
