package com.frahhs.robbing.features.handcuffing.models;

import com.frahhs.robbing.Robbing;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.UUID;

public class HandcuffingModel {
    private final Connection dbConnection;

    private final Player handcuffed;
    private final Player handcuffer;
    private Timestamp timestamp;

    public HandcuffingModel(Player handcuffed, Player handcuffer, Timestamp timestamp) {
        this.handcuffed = handcuffed;
        this.handcuffer = handcuffer;
        this.timestamp = timestamp;
        dbConnection = Robbing.getInstance().getRBDatabase().getConnection();
    }

    public Player getHandcuffed() {
        return handcuffed;
    }

    public Player getHandcuffer() {
        return handcuffer;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

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

            return new HandcuffingModel(handcuffed, handcuffer, timestamp);
        } catch ( Exception e ) {
            Robbing.getInstance().getRBLogger().error("%s: %s",e.getClass().getName(), e.getMessage());
        }

        return null;
    }
}
