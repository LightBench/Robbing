package com.frahhs.robbing.item;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.features.BaseModel;
import org.bukkit.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RobbingBlock extends BaseModel {
    private RobbingItem item;
    private Location location;

    private final Connection dbConnection;

    public RobbingBlock(RobbingItem item, Location location) {
        this.item = item;
        this.location = location;

        dbConnection = Robbing.getInstance().getRBDatabase().getConnection();
    }

    public void setItem(RobbingItem item) {
        this.item = item;
    }

    public RobbingItem getItem() {
        return item;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void save() {
        try {
            PreparedStatement ps;
            ps = dbConnection.prepareStatement("INSERT INTO BlocksPlaced (itemName, world, blockX, blockY, blockZ) VALUES (?, ?, ?, ?, ?);");
            ps.setString(1, item.getItemName());
            ps.setString(2, location.getWorld().getName());
            ps.setInt(3, location.getBlockX());
            ps.setInt(4, location.getBlockY());
            ps.setInt(5, location.getBlockZ());
            ps.executeUpdate();
            dbConnection.commit();
            ps.close();
        } catch ( Exception e ) {
            Robbing.getInstance().getRBLogger().error("%s: %s",e.getClass().getName(), e.getMessage());
        }
    }

    public void remove() {
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

                return new RobbingBlock(item, location);
            }
            ps.close();
        } catch ( Exception e ) {
            Robbing.getInstance().getRBLogger().error("%s: %s",e.getClass().getName(), e.getMessage());
        }
        return null;
    }
}
