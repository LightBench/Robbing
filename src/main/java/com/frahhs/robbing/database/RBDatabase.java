package com.frahhs.robbing.database;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.managers.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Class for managing the connection to the robbing database.
 */
public class RBDatabase {
    private final JavaPlugin plugin;
    private final String sqlite_path;
    private final String mysql_address;
    private final String mysql_port;
    private final String mysql_username;
    private final String mysql_password;
    private final String db_name;
    private Connection dbConnection = null;
    private final ConfigManager configManager = Robbing.getInstance().getConfigManager();

    /**
     * Enum representing the types of supported databases.
     */
    public enum DBType {
        SQLITE,
        MYSQL
    }

    /**
     * Constructor for RBDatabase.
     *
     * @param plugin The main JavaPlugin instance.
     */
    public RBDatabase(JavaPlugin plugin) {
        this.plugin = plugin;
        db_name = configManager.getString("database.database_name");
        sqlite_path = this.plugin.getDataFolder().getAbsolutePath() + "/data/" + db_name + ".db";
        mysql_address = configManager.getString("database.mysql.address");
        mysql_port = configManager.getString("database.mysql.port");
        mysql_username = configManager.getString("database.mysql.username");
        mysql_password = configManager.getString("database.mysql.password");
        String db_type = configManager.getString("database.type");

        if (Objects.equals(db_type, "SQLite")) {
            // Create data folder if not exist
            File data_folder = new File(Robbing.getPlugin(Robbing.class).getDataFolder().getAbsolutePath() + "/data");
            if (!data_folder.exists())
                if (data_folder.mkdir())
                    Robbing.getInstance().getRBLogger().info("Database folder created!");

            // Create SQLite connection
            createConnection(DBType.SQLITE);
        } else if (Objects.equals(db_type, "MySQL")) {
            // Create MySQL connection
            createConnection(DBType.MYSQL);
        } else {
            Robbing.getInstance().getRBLogger().error("Database type %s selected in the config is not valid, you must choose SQLite or MySQL", db_type);
            this.plugin.getPluginLoader().disablePlugin(Robbing.getInstance());
        }
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
}
