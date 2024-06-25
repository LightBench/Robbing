package com.frahhs.robbing.adapter;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.block.RobbingBlock;
import com.frahhs.robbing.feature.safe.mcp.SafeInventory;
import com.frahhs.robbing.feature.safe.mcp.SafeModel;
import com.frahhs.robbing.item.RobbingMaterial;
import com.frahhs.robbing.util.ItemUtil;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.sql.*;
import java.util.Objects;

/**
 * Armor stand to Item Display migration for robbing blocks
 * <p>
 * 2.0.1 to 2.0.2 adaption
 */
class ItemDisplayAdapter implements Listener {
    static void adapt() {
        Connection connection = Robbing.getInstance().getRobbingDatabase().getConnection();
        adaptDatabase(connection);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteract(PlayerInteractEvent e) {
        if(!Objects.equals(e.getHand(), EquipmentSlot.HAND))
            return;

        if(e.getClickedBlock() == null)
            return;

        if(!RobbingBlock.isRobbingBlock(e.getClickedBlock().getLocation()))
            return;

        Location location = e.getClickedBlock().getLocation();

        for(Entity entity : location.getWorld().getEntities()) {
            if (entity.getType() == EntityType.ARMOR_STAND) {
                if (entity.getLocation().getBlock().getLocation().equals(location)) {
                    adaptExistingBlock(
                            Robbing.getInstance().getRobbingDatabase().getConnection(),
                            entity.getLocation().getBlock().getLocation(),
                            (ArmorStand) entity
                    );
                }
            }
        }
    }

    private static void adaptExistingBlock(Connection connection, Location location, ArmorStand armorStand) {
        String tableName = "BlocksPlaced";
        String armorStandUUIDColumn = "armorStandUUID";
        String entityUUIDColumn = "entityUUID";

        try {
            if(!isColumnExists(connection, tableName, armorStandUUIDColumn)) {
                return;
            }
        } catch(SQLException e) {
            Robbing.getRobbingLogger().error("error while performing Item Display adaption\n%s", e);
        }

        // Place the Item Display
        Location armorStandLocation = armorStand.getLocation();
        armorStandLocation.add(0, 0.5, 0);
        ItemDisplay itemDisplay = Objects.requireNonNull(armorStandLocation.getWorld()).spawn(armorStandLocation, ItemDisplay.class);
        itemDisplay.setItemStack(Robbing.getInstance().getItemsManager().get(RobbingMaterial.SAFE).getItemStack());
        itemDisplay.setBrightness(new Display.Brightness(15, 15));

        // Save the Item Display
        String newEntityUUID = itemDisplay.getUniqueId().toString();
        try {
            updateEntityUUID(connection, tableName, entityUUIDColumn, newEntityUUID, location);
        } catch(SQLException e) {
            Robbing.getRobbingLogger().error("error while performing Item Display adaption\n%s", e);
        }

        // Save the safe inventory and pin
        RobbingBlock safe = RobbingBlock.getFromLocation(armorStand.getLocation().getBlock().getLocation());
        SafeModel safeModel = SafeModel.getFromSafe(safe);

        PersistentDataContainer container = armorStand.getPersistentDataContainer();
        NamespacedKey inventoryKey = new NamespacedKey(Robbing.getInstance(), "inventory");
        NamespacedKey pinKey = new NamespacedKey(Robbing.getInstance(), "pin");

        if(container.has(inventoryKey, PersistentDataType.STRING)) {
            SafeInventory safeInventory = new SafeInventory(safe);
            String base64Inventory = container.get(inventoryKey, PersistentDataType.STRING);
            safeInventory.getInventory().setContents(ItemUtil.fromBase64(base64Inventory));
            safeModel.saveInventory(safeInventory.getInventory());
        }

        if(container.has(pinKey, PersistentDataType.STRING)) {
            String pin = container.get(pinKey, PersistentDataType.STRING);
            safeModel.savePin(pin);
        }

        // Remove the armor stand
        armorStand.remove();
    }

    // Method to update the entityUUID column with the new UUID
    private static void updateEntityUUID(Connection connection, String tableName, String columnName, String newEntityUUID, Location location) throws SQLException {
        String sql = "UPDATE " + tableName + " SET " + columnName + " = ? WHERE world = ? AND blockX = ? AND blockY = ? AND blockZ = ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, newEntityUUID);
            preparedStatement.setString(2, location.getWorld().getName());
            preparedStatement.setInt(3, location.getBlockX());
            preparedStatement.setInt(4, location.getBlockY());
            preparedStatement.setInt(5, location.getBlockZ());
            preparedStatement.executeUpdate();
            Robbing.getRobbingLogger().fine("Updated " + columnName + " to " + newEntityUUID);
            connection.commit();
        }
    }

    // Method to update the entityUUID column with the new UUID
    private static void updateArmorStandUUID(Connection connection, String tableName, Location location) throws SQLException {
        String sql = "UPDATE " + tableName + " SET " + "armorStandUUID" + " = ? WHERE world = ? AND blockX = ? AND blockY = ? AND blockZ = ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, null);
            preparedStatement.setString(2, location.getWorld().getName());
            preparedStatement.setInt(3, location.getBlockX());
            preparedStatement.setInt(4, location.getBlockY());
            preparedStatement.setInt(5, location.getBlockZ());
            preparedStatement.executeUpdate();
            connection.commit();
        }
    }

    // Method to remove a column from the specified table
    private static void removeColumn(Connection connection, String tableName, String columnName) throws SQLException {
        String sql = "ALTER TABLE " + tableName + " DROP COLUMN " + columnName;
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
            connection.commit();
            Robbing.getRobbingLogger().fine("Column " + columnName + " removed from the table " + tableName);
        }
    }

    private static void adaptDatabase(Connection connection) {
        String tableName = "BlocksPlaced";
        String armorStandUUIDColumn = "armorStandUUID";
        String entityUUIDColumn = "entityUUID";
        String columnDefinition = "CHAR(100)";

        try {
            if (!isColumnExists(connection, tableName, entityUUIDColumn)) {
                // Add the column if it does not exist
                renameColumn(connection, tableName, armorStandUUIDColumn, entityUUIDColumn);
                addColumn(connection, tableName, armorStandUUIDColumn, columnDefinition);
            }
        } catch(SQLException e) {
            Robbing.getRobbingLogger().error("error while performing Item Display adaption\n%s", e);
        }
    }

    // Method to check if a column exists in the specified table
    private static boolean isColumnExists(Connection connection, String tableName, String columnName) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        try (ResultSet columns = metaData.getColumns(null, null, tableName, columnName)) {
            return columns.next();
        }
    }

    // Method to add a column to the specified table
    private static void addColumn(Connection connection, String tableName, String columnName, String columnDefinition) throws SQLException {
        String sql = "ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " " + columnDefinition;
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
            connection.commit();
            Robbing.getRobbingLogger().fine("Column " + columnName + " added to the table " + tableName);
        }
    }

    public static void renameColumn(Connection connection, String tableName, String oldColumnName, String newColumnName) throws SQLException {
        String sql = "ALTER TABLE " + tableName + " RENAME COLUMN " + oldColumnName + " TO " + newColumnName;

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
            Robbing.getRobbingLogger().fine("Renamed column " + oldColumnName + " to " + newColumnName + " in table " + tableName);
        }
    }
}
