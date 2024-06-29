package com.frahhs.robbing.feature.safe.database;

import com.frahhs.lightlib.LightPlugin;

import java.sql.Connection;
import java.sql.Statement;

public class SafeDatabase {
    /**
     * Creates the safe inventories table if it does not exist.
     */
    public static void createSafeInventoryTable() {
        Statement stmt;
        Connection connection = LightPlugin.getLightDatabase().getConnection();

        // if table safes not exist create it
        try {
            stmt = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS safeInventory (" +
                    "safeUUID CHAR(100) PRIMARY KEY,"            +
                    "timestamp DEFAULT CURRENT_TIMESTAMP,"       +
                    "inventory TEXT NOT NULL)"                   ;
            stmt.executeUpdate(sql);
            connection.commit();
            stmt.close();
        } catch ( Exception e ) {
            LightPlugin.getLightLogger().error("Error while creating safeInventory table, %s", e);
        }
    }

    /**
     * Creates the safe inventories table if it does not exist.
     */
    public static void createSafeLockedTable() {
        Statement stmt;
        Connection connection = LightPlugin.getLightDatabase().getConnection();

        // if table safes not exist create it
        try {
            stmt = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS SafeLocked (" +
                    "safeUUID CHAR(100) PRIMARY KEY,"         +
                    "timestamp DEFAULT CURRENT_TIMESTAMP,"    +
                    "playerUUID CHAR(100) NOT NULL,"          +
                    "pin CHAR(100) NOT NULL)"                 ;
            stmt.executeUpdate(sql);
            connection.commit();
            stmt.close();
        } catch ( Exception e ) {
            LightPlugin.getLightLogger().error("Error while creating SafeLocked table, %s", e);
        }
    }
}
