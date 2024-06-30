package com.frahhs.robbing.feature.handcuffing.database;

import com.frahhs.lightlib.LightPlugin;

import java.sql.Connection;
import java.sql.Statement;

public class HandcuffingDatabase {
    /**
     * Creates the handcuffing table if it does not exist.
     */
    public static void createHandcuffingTable() {
        Statement stmt;
        Connection connection = LightPlugin.getLightDatabase().getConnection();

        // if table safes not exist create it
        try {
            stmt = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Handcuffing (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT,"    +
                    "handcuffed CHAR(100) NOT NULL,"           +
                    "handcuffer CHAR(100) NOT NULL,"           +
                    "timestamp DEFAULT CURRENT_TIMESTAMP)"     ;
            stmt.executeUpdate(sql);
            connection.commit();
            stmt.close();
        } catch ( Exception e ) {
            LightPlugin.getLightLogger().error("Error while creating handcuffing table, %s", e);
        }
    }
}
