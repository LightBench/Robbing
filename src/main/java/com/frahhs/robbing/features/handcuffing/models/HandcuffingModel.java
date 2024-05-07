package com.frahhs.robbing.features.handcuffing.models;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.features.BaseModel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.UUID;

/**
 * Model class representing handcuffing data.
 */
public class HandcuffingModel extends BaseModel {
    private final Connection dbConnection;

    private final Player handcuffed;
    private final Player handcuffer;
    private Timestamp timestamp;

    /**
     * Constructs a HandcuffingModel instance.
     *
     * @param handcuffed The player who is handcuffed.
     * @param handcuffer The player who handcuffed.
     * @param timestamp The timestamp of the handcuffing event.
     */
    public HandcuffingModel(Player handcuffer, Player handcuffed, Timestamp timestamp) {
        this.handcuffed = handcuffed;
        this.handcuffer = handcuffer;
        this.timestamp = timestamp;
        dbConnection = Robbing.getInstance().getRBDatabase().getConnection();
    }

    /**
     * Retrieves the handcuffed player.
     *
     * @return The handcuffed player.
     */
    public Player getHandcuffed() {
        return handcuffed;
    }

    /**
     * Retrieves the handcuffer.
     *
     * @return The handcuffer.
     */
    public Player getHandcuffer() {
        return handcuffer;
    }

    /**
     * Retrieves the timestamp of the handcuffing event.
     *
     * @return The timestamp.
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }

    /**
     * Saves the handcuffing event to the database.
     */
    public void save() {
        String handcuffedUUID = handcuffed.getUniqueId().toString();
        String handcufferUUID = handcuffer.getUniqueId().toString();

        try {
            PreparedStatement ps;
            ps = dbConnection.prepareStatement("INSERT INTO handcuffing (handcuffed, handcuffer, timestamp) VALUES (?, ?, ?);");
            ps.setString(1, handcuffedUUID);
            ps.setString(2, handcufferUUID);
            ps.setTimestamp(3, timestamp);
            ps.executeUpdate();
            dbConnection.commit();
            ps.close();
        } catch ( Exception e ) {
            Robbing.getInstance().getRBLogger().error("%s: %s",e.getClass().getName(), e.getMessage());
        }
    }

    /**
     * Removes the handcuffing event from the database.
     */
    public void remove() {
        String handcuffedUUID = handcuffed.getUniqueId().toString();

        try {
            PreparedStatement ps;
            ps = dbConnection.prepareStatement("DELETE FROM handcuffing WHERE handcuffed = ?;");
            ps.setString(1, handcuffedUUID);
            ps.executeUpdate();
            dbConnection.commit();
            ps.close();
        } catch ( Exception e ) {
            Robbing.getInstance().getRBLogger().error("%s: %s",e.getClass().getName(), e.getMessage());
        }
    }

    /**
     * Checks if a player is handcuffed.
     *
     * @param handcuffed The player to check.
     * @return True if the player is handcuffed, false otherwise.
     */
    public static boolean isHandcuffed(Player handcuffed) {
        Connection dbConnection = Robbing.getInstance().getRBDatabase().getConnection();

        try {
            Statement stmt = dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM handcuffing");
            while ( rs.next() ) {
                String handcuffedPlayerUUID = rs.getString("handcuffed");
                Player handcuffedPlayer = Bukkit.getPlayer(UUID.fromString(handcuffedPlayerUUID));
                if(handcuffed.equals(handcuffedPlayer))
                    return true;
            }
            dbConnection.commit();
            stmt.close();
        } catch ( Exception e ) {
            Robbing.getInstance().getRBLogger().error("%s: %s",e.getClass().getName(), e.getMessage());
        }
        return false;
    }

    /**
     * Retrieves a HandcuffingModel instance based on the handcuffed player.
     *
     * @param handcuffed The handcuffed player.
     * @return The HandcuffingModel instance.
     */
    public static HandcuffingModel getFromHandcuffed(Player handcuffed) {
        Connection dbConnection = Robbing.getInstance().getRBDatabase().getConnection();
        String handcuffedUUID = handcuffed.getUniqueId().toString();

        try {
            PreparedStatement ps;
            ps = dbConnection.prepareStatement("SELECT * FROM handcuffing WHERE handcuffed = ?");
            ps.setString(1, handcuffedUUID);
            ResultSet rs = ps.executeQuery();

            rs.next();
            String handcufferPlayerUUID = rs.getString("handcuffer");
            Timestamp timestamp = rs.getTimestamp("timestamp");

            dbConnection.commit();
            ps.close();

            Player handcuffer = Bukkit.getPlayer(UUID.fromString(handcufferPlayerUUID));

            return new HandcuffingModel(handcuffer, handcuffed, timestamp);
        } catch ( Exception e ) {
            Robbing.getInstance().getRBLogger().error("%s: %s",e.getClass().getName(), e.getMessage());
        }

        return null;
    }
}
