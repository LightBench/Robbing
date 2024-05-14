package com.frahhs.robbing.feature.handcuffing.provider;

import com.frahhs.robbing.feature.handcuffing.bag.HandcuffingCooldownBag;
import com.frahhs.robbing.feature.BaseProvider;
import com.frahhs.robbing.feature.handcuffing.model.Handcuffing;
import com.frahhs.robbing.util.Cooldown;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.UUID;

public class HandcuffingProvider extends BaseProvider {
    /**
     * Checks if a player is currently handcuffed.
     *
     * @param handcuffed The player to check.
     * @return True if the player is handcuffed, false otherwise.
     */
    public boolean isHandcuffed(Player handcuffed) {
        try {
            Statement stmt = dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Handcuffing");
            while ( rs.next() ) {
                String handcuffedPlayerUUID = rs.getString("handcuffed");
                Player handcuffedPlayer = Bukkit.getPlayer(UUID.fromString(handcuffedPlayerUUID));
                if(handcuffed.equals(handcuffedPlayer))
                    return true;
            }
            dbConnection.commit();
            stmt.close();
        } catch ( Exception e ) {
            logger.error("%s: %s",e.getClass().getName(), e.getMessage());
        }
        return false;
    }

    /**
     * Saves the handcuffing event to the database.
     */
    public void saveHandcuffing(Player handcuffer, Player handcuffed) {
        String handcuffedUUID = handcuffed.getUniqueId().toString();
        String handcufferUUID = handcuffer.getUniqueId().toString();

        try {
            PreparedStatement ps;
            ps = dbConnection.prepareStatement("INSERT INTO handcuffing (handcuffed, handcuffer) VALUES (?, ?);");
            ps.setString(1, handcuffedUUID);
            ps.setString(2, handcufferUUID);
            ps.executeUpdate();
            dbConnection.commit();
            ps.close();
        } catch ( Exception e ) {
            logger.error("%s: %s",e.getClass().getName(), e.getMessage());
        }
    }

    /**
     * Removes the handcuffing event from the database.
     */
    public void deleteHandcuffing(Player handcuffed) {
        String handcuffedUUID = handcuffed.getUniqueId().toString();

        try {
            PreparedStatement ps;
            ps = dbConnection.prepareStatement("DELETE FROM Handcuffing WHERE handcuffed = ?;");
            ps.setString(1, handcuffedUUID);
            ps.executeUpdate();
            dbConnection.commit();
            ps.close();
        } catch ( Exception e ) {
            logger.error("%s: %s",e.getClass().getName(), e.getMessage());
        }
    }

    /**
     * Retrieves a HandcuffingModel instance based on the handcuffed player.
     *
     * @param handcuffed The handcuffed player.
     * @return The HandcuffingModel instance, or null if not found.
     */
    public Handcuffing getHandcuffing(Player handcuffed) {
        String handcuffedUUID = handcuffed.getUniqueId().toString();

        try {
            PreparedStatement ps;
            ps = dbConnection.prepareStatement("SELECT * FROM Handcuffing WHERE handcuffed = ?");
            ps.setString(1, handcuffedUUID);
            ResultSet rs = ps.executeQuery();

            rs.next();
            String handcufferPlayerUUID = rs.getString("handcuffer");
            Timestamp timestamp = rs.getTimestamp("timestamp");

            dbConnection.commit();
            ps.close();

            Player handcuffer = Bukkit.getPlayer(UUID.fromString(handcufferPlayerUUID));

            return new Handcuffing(handcuffer, handcuffed);
        } catch ( Exception e ) {
            logger.error("%s: %s",e.getClass().getName(), e.getMessage());
        }

        return null;
    }

    /**
     * Retrieves a HandcuffingModel instance based on the handcuffed player.
     *
     * @param handcuffed The handcuffed player.
     * @return The HandcuffingModel instance, or null if not found.
     */
    public Timestamp getTimestamp(Player handcuffed) {
        String handcuffedUUID = handcuffed.getUniqueId().toString();

        try {
            PreparedStatement ps;
            ps = dbConnection.prepareStatement("SELECT * FROM Handcuffing WHERE handcuffed = ?");
            ps.setString(1, handcuffedUUID);
            ResultSet rs = ps.executeQuery();
            rs.next();
            Timestamp timestamp = rs.getTimestamp("timestamp");
            ps.close();

            return timestamp;
        } catch ( Exception e ) {
            logger.error("%s: %s",e.getClass().getName(), e.getMessage());
        }

        return null;
    }

    /**
     * Checks if a player is currently under a handcuffing cooldown.
     *
     * @param handcuffer The player to check.
     * @return True if the player is under cooldown, otherwise false.
     */
    public boolean haveCooldown(Player handcuffer) {
        HandcuffingCooldownBag handcuffingCooldownBag = (HandcuffingCooldownBag)bagManager.getBag("HandcuffingCooldownBag");
        return handcuffingCooldownBag.getMap().containsKey(handcuffer);
    }

    /**
     * Retrieves the cooldown timestamp for a player.
     *
     * @param handcuffer The player to check.
     * @return The timestamp when the handcuffing action occurred.
     */
    public Cooldown getCooldown(Player handcuffer) {
        HandcuffingCooldownBag handcuffingCooldownBag = (HandcuffingCooldownBag)bagManager.getBag("HandcuffingCooldownBag");
        return handcuffingCooldownBag.getMap().get(handcuffer);
    }

    /**
     * Sets the cooldown for the handcuffing action.
     */
    public void saveCooldown(Player handcuffer, Cooldown cooldown) {
        HandcuffingCooldownBag handcuffingCooldownBag = (HandcuffingCooldownBag)bagManager.getBag("HandcuffingCooldownBag");
        handcuffingCooldownBag.getMap().put(handcuffer, cooldown);
    }

    /**
     * Remove the cooldown for the handcuffing action.
     */
    public void removeCooldown(Player handcuffer) {
        HandcuffingCooldownBag handcuffingCooldownBag = (HandcuffingCooldownBag)bagManager.getBag("HandcuffingCooldownBag");
        handcuffingCooldownBag.getMap().remove(handcuffer);
    }
}
