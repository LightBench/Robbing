package com.frahhs.robbing.database;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.provider.ConfigProvider;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

/**
 * Class for managing the connection to the robbing database.
 */
public class RobbingDatabase {
    private final Robbing plugin;
    private final String sqlite_path;
    private final String mysql_address;
    private final String mysql_port;
    private final String mysql_username;
    private final String mysql_password;
    private final String db_name;
    private final String db_type;
    private Connection dbConnection = null;
    private final ConfigProvider configProvider;

    /**
     * Enum representing the types of supported databases.
     */
    public enum DBType {
        SQLITE,
        MYSQL
    }

    /**
     * Constructor for RobbingDatabase.
     *
     * @param plugin The main JavaPlugin instance.
     */
    public RobbingDatabase(Robbing plugin) {
        this.plugin = plugin;
        configProvider = plugin.getConfigProvider();

        // Setup variables
        db_name = configProvider.getString("database.database-name");
        sqlite_path = this.plugin.getDataFolder().getAbsolutePath() + "/data/" + db_name + ".db";
        mysql_address = configProvider.getString("database.mysql.address");
        mysql_port = configProvider.getString("database.mysql.port");
        mysql_username = configProvider.getString("database.mysql.username");
        mysql_password = configProvider.getString("database.mysql.password");
        db_type = configProvider.getString("database.type");

        // Setup connection
        if (Objects.equals(db_type, "SQLite")) {
            // Create data folder if not exist
            File data_folder = new File(Robbing.getPlugin(Robbing.class).getDataFolder().getAbsolutePath() + "/data");
            if (!data_folder.exists())
                if (data_folder.mkdir())
                    plugin.getRobbingLogger().info("Database folder created!");

            // Create SQLite connection
            createConnection(DBType.SQLITE);
        } else if (Objects.equals(db_type, "MySQL")) {
            // Create MySQL connection
            createConnection(DBType.MYSQL);
        } else {
            plugin.getRobbingLogger().error("Database type %s selected in the config is not valid, you must choose SQLite or MySQL", db_type);
            this.plugin.getPluginLoader().disablePlugin(plugin);
        }

        // Setup tables
        handcuffingTable();
        blocksPlacedTable();
    }

    /**
     * Retrieves the current database connection.
     *
     * @return The current database connection.
     */
    public Connection getConnection() {
        return dbConnection;
    }

    /**
     * Creates a connection to the specified database type.
     *
     * @param databaseType The type of database (SQLite or MySQL).
     */
    private void createConnection(DBType databaseType) {
        try {
            if (databaseType == DBType.SQLITE) {
                // Connect to SQLite database
                Class.forName("org.sqlite.JDBC");
                dbConnection = DriverManager.getConnection("jdbc:sqlite:" + sqlite_path);
                dbConnection.setAutoCommit(false);
            } else if (databaseType == DBType.MYSQL) {
                // Connect to MySQL database
                Class.forName("com.mysql.jdbc.Driver");
                dbConnection = DriverManager.getConnection("jdbc:mysql://" + mysql_address + ":" + mysql_port, mysql_username, mysql_password);
                dbConnection.setAutoCommit(false);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Disables the current database connection.
     */
    public void disable() {
        if(dbConnection == null)
            return;

        try {
            if(dbConnection.isClosed())
                return;

            dbConnection.close();
        } catch (SQLException e) {
            plugin.getRobbingLogger().error(e.toString());
        }
    }

    /**
     * Creates the handcuffing table if it does not exist.
     */
    public void handcuffingTable() {
        Statement stmt;

        // if table safes not exist create it
        try {
            stmt = dbConnection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Handcuffing (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT,"    +
                         "handcuffed CHAR(100) NOT NULL,"           +
                         "handcuffer CHAR(100) NOT NULL,"           +
                         "timestamp DEFAULT CURRENT_TIMESTAMP)"            ;
            stmt.executeUpdate(sql);
            dbConnection.commit();
            stmt.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    /**
     * Creates the handcuffing table if it does not exist.
     */
    public void blocksPlacedTable() {
        Statement stmt;

        // if table safes not exist create it
        try {
            stmt = dbConnection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS BlocksPlaced (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT,"     +
                         "timestamp DEFAULT CURRENT_TIMESTAMP,"      +
                         "placer CHAR(100),"                         +
                         "material CHAR(100) NOT NULL,"              +
                         "armorStandUUID CHAR(100) NOT NULL,"        +
                         "world CHAR(100) NOT NULL,"                 +
                         "blockX int NOT NULL,"                      +
                         "blockY int NOT NULL,"                      +
                         "blockZ int NOT NULL)"                      ;
            stmt.executeUpdate(sql);
            dbConnection.commit();
            stmt.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }
}
