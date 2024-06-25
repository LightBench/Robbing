package com.frahhs.robbing.feature.safe.mcp;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.block.RobbingBlock;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides methods to manage the safeInventory table in the database.
 */
public class SafeInventoryProvider {
    private static final String TABLE_NAME = "safeInventory";
    private final Connection connection;

    /**
     * Constructor that initializes the SafeInventoryProvider with a database connection
     * and creates the safeInventory table if it does not exist.
     */
    public SafeInventoryProvider() {
        this.connection = Robbing.getInstance().getRobbingDatabase().getConnection();
    }

    /**
     * Adds a new entry to the safeInventory table.
     *
     * @param safeUUID  the unique identifier for the safe
     * @param inventory the inventory data
     */
    public void addEntry(String safeUUID, String inventory) {
        String sql = "INSERT INTO " + TABLE_NAME + " (safeUUID, inventory) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, safeUUID);
            preparedStatement.setString(2, inventory);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves an entry from the safeInventory table by safeUUID.
     *
     * @param safeUUID the unique identifier for the safe
     * @return a SafeInventoryEntry object representing the entry, or null if not found
     */
    public SafeInventoryEntry getEntryByUUID(String safeUUID) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE safeUUID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, safeUUID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new SafeInventoryEntry(
                            resultSet.getString("safeUUID"),
                            resultSet.getTimestamp("timestamp"),
                            resultSet.getString("inventory")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves all entries from the safeInventory table.
     *
     * @return a list of SafeInventoryEntry objects representing all entries in the table
     */
    public List<SafeInventoryEntry> getAllEntries() {
        List<SafeInventoryEntry> entries = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_NAME;
        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                entries.add(new SafeInventoryEntry(
                        resultSet.getString("safeUUID"),
                        resultSet.getTimestamp("timestamp"),
                        resultSet.getString("inventory")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entries;
    }

    /**
     * Updates an entry in the safeInventory table by safeUUID.
     *
     * @param safeUUID  the unique identifier for the safe
     * @param inventory the new inventory data
     */
    public void updateEntry(String safeUUID, String inventory) {
        String sql = "UPDATE " + TABLE_NAME + " SET inventory = ? WHERE safeUUID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, inventory);
            preparedStatement.setString(2, safeUUID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes an entry from the safeInventory table by safeUUID.
     *
     * @param safeUUID the unique identifier for the safe
     */
    public void removeEntry(String safeUUID) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE safeUUID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, safeUUID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Represents an entry in the safeInventory table.
     */
    public static class SafeInventoryEntry {
        private final String safeUUID;
        private final Timestamp timestamp;
        private final String inventory;

        /**
         * Constructor for creating a SafeInventoryEntry object.
         *
         * @param safeUUID  the unique identifier for the safe
         * @param timestamp the timestamp of the entry
         * @param inventory the inventory data
         */
        public SafeInventoryEntry(String safeUUID, Timestamp timestamp, String inventory) {
            this.safeUUID = safeUUID;
            this.timestamp = timestamp;
            this.inventory = inventory;
        }

        /**
         * Gets the unique identifier for the safe.
         *
         * @return the safeUUID
         */
        public String getSafeUUID() {
            return safeUUID;
        }

        /**
         * Gets the timestamp of the entry.
         *
         * @return the timestamp
         */
        public Timestamp getTimestamp() {
            return timestamp;
        }

        /**
         * Gets the inventory data.
         *
         * @return the inventory
         */
        public String getInventory() {
            return inventory;
        }

        @Override
        public String toString() {
            return "SafeInventoryEntry{" +
                    "safeUUID='" + safeUUID + '\'' +
                    ", timestamp=" + timestamp +
                    ", inventory='" + inventory + '\'' +
                    '}';
        }
    }
}