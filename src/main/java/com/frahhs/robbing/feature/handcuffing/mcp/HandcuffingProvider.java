package com.frahhs.robbing.feature.handcuffing.mcp;

import com.frahhs.robbing.feature.Provider;
import com.frahhs.robbing.feature.handcuffing.bag.HandcuffingCooldownBag;
import com.frahhs.robbing.feature.handcuffing.bag.JustHandcuffBag;
import com.frahhs.robbing.util.Cooldown;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * Provider class for managing handcuffing-related data and database operations.
 */
class HandcuffingProvider extends Provider {
    private final HandcuffingCooldownBag handcuffingCooldownBag;
    private final JustHandcuffBag justHandcuffBag;

    /**
     * Constructs a HandcuffingProvider instance.
     */
    protected HandcuffingProvider() {
        handcuffingCooldownBag = (HandcuffingCooldownBag) bagManager.getBag("HandcuffingCooldownBag");
        justHandcuffBag = (JustHandcuffBag) bagManager.getBag("JustHandcuffBag");
    }

    /**
     * Checks if a player is currently handcuffed.
     *
     * @param handcuffed The player to check.
     * @return True if the player is handcuffed, false otherwise.
     */
    protected boolean isHandcuffed(Player handcuffed) {
        try {
            Statement stmt = dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Handcuffing");
            while (rs.next()) {
                String handcuffedPlayerUUID = rs.getString("handcuffed");
                Player handcuffedPlayer = Bukkit.getPlayer(UUID.fromString(handcuffedPlayerUUID));
                if (handcuffed.equals(handcuffedPlayer))
                    return true;
            }
            dbConnection.commit();
            stmt.close();
        } catch (Exception e) {
            logger.error("%s: %s", e.getClass().getName(), e.getMessage());
        }
        return false;
    }

    /**
     * Saves the handcuffing event to the database.
     */
    protected void saveHandcuffing(Player handcuffer, Player handcuffed) {
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
        } catch (Exception e) {
            logger.error("%s: %s", e.getClass().getName(), e.getMessage());
        }
    }

    /**
     * Removes the handcuffing event from the database.
     */
    protected void deleteHandcuffing(Player handcuffed) {
        String handcuffedUUID = handcuffed.getUniqueId().toString();

        try {
            PreparedStatement ps;
            ps = dbConnection.prepareStatement("DELETE FROM Handcuffing WHERE handcuffed = ?;");
            ps.setString(1, handcuffedUUID);
            ps.executeUpdate();
            dbConnection.commit();
            ps.close();
        } catch (Exception e) {
            logger.error("%s: %s", e.getClass().getName(), e.getMessage());
        }
    }

    /**
     * Retrieves a HandcuffingModel instance based on the handcuffed player.
     *
     * @param handcuffed The handcuffed player.
     * @return The HandcuffingModel instance, or null if not found.
     */
    protected Handcuffing getHandcuffing(Player handcuffed) {
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
        } catch (Exception e) {
            logger.error("%s: %s", e.getClass().getName(), e.getMessage());
        }

        return null;
    }

    /**
     * Retrieves the timestamp of the handcuffing event.
     *
     * @param handcuffed The player who was handcuffed.
     * @return The timestamp of the handcuffing event.
     */
    protected Timestamp getTimestamp(Player handcuffed) {
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
        } catch (Exception e) {
            logger.error("%s: %s", e.getClass().getName(), e.getMessage());
        }

        return null;
    }

    /**
     * Checks if a player is currently under a handcuffing cooldown.
     *
     * @param handcuffer The player to check.
     * @return True if the player is under cooldown, otherwise false.
     */
    protected boolean haveCooldown(Player handcuffer) {
        return handcuffingCooldownBag.getData().containsKey(handcuffer);
    }

    /**
     * Retrieves the cooldown timestamp for a player.
     *
     * @param handcuffer The player to check.
     * @return The timestamp when the handcuffing action occurred.
     */
    protected Cooldown getCooldown(Player handcuffer) {
        return handcuffingCooldownBag.getData().get(handcuffer);
    }

    /**
     * Sets the cooldown for the handcuffing action.
     *
     * @param handcuffer The player to set the cooldown for.
     * @param cooldown   The cooldown duration.
     */
    protected void saveCooldown(Player handcuffer, Cooldown cooldown) {
        handcuffingCooldownBag.getData().put(handcuffer, cooldown);
    }

    /**
     * Removes the cooldown for the handcuffing action.
     *
     * @param handcuffer The player to remove the cooldown from.
     */
    protected void removeCooldown(Player handcuffer) {
        handcuffingCooldownBag.getData().remove(handcuffer);
    }

    protected void setJustHandcuff(Player player, boolean value) {
        if(value) {
            if(!justHandcuffBag.getData().contains(player)) {
                justHandcuffBag.getData().add(player);
            }
        } else {
            justHandcuffBag.getData().remove(player);
        }

    }

    protected boolean isJustHandcuff(Player player) {
        return justHandcuffBag.getData().contains(player);
    }
}
