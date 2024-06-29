package com.frahhs.robbing.feature.safe.mcp;

import com.frahhs.lightlib.LightProvider;
import com.frahhs.lightlib.block.LightBlock;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * SafePinProvider class that extends LightProvider and manages the SafeLocked table.
 */
public class SafePinProvider extends LightProvider {
    /**
     * Creates a new safe entry in the SafeLocked table.
     *
     * @param safeUUID The unique identifier for the safe.
     * @param playerUUID The unique identifier for the player.
     * @param pin The pin code to be stored for the safe.
     * @return true if the safe entry was created successfully, false otherwise.
     */
    protected boolean addLockedSafe(UUID safeUUID, UUID playerUUID, String pin) {
        String sql = "INSERT INTO SafeLocked (safeUUID, playerUUID, pin) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = dbConnection.prepareStatement(sql)) {
            pstmt.setString(1, safeUUID.toString());
            pstmt.setString(2, playerUUID.toString());
            pstmt.setString(3, pin);
            pstmt.executeUpdate();
            dbConnection.commit();
            return true;
        } catch (SQLException e) {
            logger.error("Failed to create safe entry for UUID: " + safeUUID, e);
            try {
                dbConnection.rollback();
            } catch (SQLException rollbackEx) {
                logger.error("Failed to rollback transaction", rollbackEx);
            }
            return false;
        }
    }

    /**
     * Retrieves the pin code for a given safeUUID from the SafeLocked table.
     *
     * @param safeUUID The unique identifier for the safe.
     * @return The pin code as a String if found, null otherwise.
     */
    protected String getSafePin(UUID safeUUID) {
        String sql = "SELECT pin FROM SafeLocked WHERE safeUUID = ?";
        try (PreparedStatement pstmt = dbConnection.prepareStatement(sql)) {
            pstmt.setString(1, safeUUID.toString());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("pin");
            }
        } catch (SQLException e) {
            logger.error("Failed to retrieve pin for safe UUID: " + safeUUID, e);
        }
        return null;
    }

    /**
     * Updates the pin code for a given safeUUID in the SafeLocked table.
     *
     * @param safeUUID The unique identifier for the safe.
     * @param newPin The new pin code to be stored for the safe.
     * @return true if the safe entry was updated successfully, false otherwise.
     */
    protected boolean updateSafe(UUID safeUUID, String newPin) {
        String sql = "UPDATE SafeLocked SET pin = ? WHERE safeUUID = ?";
        try (PreparedStatement pstmt = dbConnection.prepareStatement(sql)) {
            pstmt.setString(1, newPin);
            pstmt.setString(2, safeUUID.toString());
            int affectedRows = pstmt.executeUpdate();
            dbConnection.commit();
            return affectedRows > 0;
        } catch (SQLException e) {
            logger.error("Failed to update pin for safe UUID: " + safeUUID, e);
            try {
                dbConnection.rollback();
            } catch (SQLException rollbackEx) {
                logger.error("Failed to rollback transaction", rollbackEx);
            }
            return false;
        }
    }

    /**
     * Deletes a safe entry from the SafeLocked table.
     *
     * @param safeUUID The unique identifier for the safe.
     * @return true if the safe entry was deleted successfully, false otherwise.
     */
    protected boolean deleteSafe(UUID safeUUID) {
        String sql = "DELETE FROM SafeLocked WHERE safeUUID = ?";
        try (PreparedStatement pstmt = dbConnection.prepareStatement(sql)) {
            pstmt.setString(1, safeUUID.toString());
            int affectedRows = pstmt.executeUpdate();
            dbConnection.commit();
            return affectedRows > 0;
        } catch (SQLException e) {
            logger.error("Failed to delete safe entry for UUID: " + safeUUID, e);
            try {
                dbConnection.rollback();
            } catch (SQLException rollbackEx) {
                logger.error("Failed to rollback transaction", rollbackEx);
            }
            return false;
        }
    }

    /**
     * Retrieves all safes for a given playerUUID from the SafeLocked table.
     *
     * @param playerUUID The unique identifier for the player.
     * @return A list of safeUUIDs that belong to the player, or an empty list if none found.
     */
    protected List<SafeModel> getSafesByPlayer(UUID playerUUID) {
        List<SafeModel> safes = new ArrayList<>();
        String sql = "SELECT safeUUID FROM SafeLocked WHERE playerUUID = ?";
        try (PreparedStatement pstmt = dbConnection.prepareStatement(sql)) {
            pstmt.setString(1, playerUUID.toString());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                LightBlock safe = LightBlock.getFromUUID(UUID.fromString(rs.getString("safeUUID")));
                safes.add(SafeModel.getFromSafe(safe));
            }
        } catch (SQLException e) {
            logger.error("Failed to retrieve safes for player UUID: " + playerUUID, e);
        }
        return safes;
    }
}
