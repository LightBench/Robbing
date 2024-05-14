package com.frahhs.robbing.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;


/**
 * Provides logging functionalities for the plugin.
 */
public class RobbingLogger {
    private final JavaPlugin plugin;
    private Level level;
    private String prefix = "[Robbing] ";

    /**
     * Constructs a new RBLogger instance.
     *
     * @param plugin The JavaPlugin instance associated with this logger.
     */
    public RobbingLogger(JavaPlugin plugin) {
        this.plugin = plugin;
        level = Level.ALL;
    }

    /**
     * Retrieves the logging level.
     *
     * @return The logging level.
     */
    public Level getLevel() {
        return level;
    }

    /**
     * Sets the logging level.
     *
     * @param level The logging level to set.
     */
    public void setLevel(Level level) {
        this.level = level;
    }

    /**
     * Retrieves the prefix for log messages.
     *
     * @return The log message prefix.
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Sets the prefix for log messages.
     *
     * @param prefix The prefix to set.
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Logs a debug message if the logging level is set to ALL.
     *
     * @param message The debug message to log.
     */
    public void debug(String message, Object ...args) {
        if (level == Level.ALL)
            Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + prefix +  String.format(message, args));
    }

    /**
     * Logs an info message if the logging level is set to ALL or INFO.
     *
     * @param message The info message to log.
     */
    public void info(String message, Object ...args) {
        if (level == Level.ALL || level == Level.INFO)
            Bukkit.getConsoleSender().sendMessage(prefix +  String.format(message, args));
    }

    /**
     * Logs a warning message if the logging level is set to ALL, INFO, or WARNING.
     *
     * @param message The warning message to log.
     */
    public void warning(String message, Object ...args) {
        if (level == Level.ALL || level == Level.INFO || level == Level.WARNING)
            Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + prefix +  String.format(message, args));
    }

    /**
     * Logs an error message if the logging level is not set to OFF.
     *
     * @param message The error message to log.
     */
    public void error(String message, Object ...args) {
        if (level != Level.OFF)
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + prefix +  String.format(message, args));
    }

    /**
     * Logs an unexpected error message with a provided error code.
     * The error code should be a randomly 6 alphanumeric uppercase chars assigned identifier.
     * <a href="https://www.gigacalculator.com/randomizers/random-alphanumeric-generator.php">Generator</a>
     * Just be sure that the new code is not already in use.
     *
     * @param code The randomly assigned error code.
     */
    public void unexpectedError(String code) {
        String message = "====================================================";
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + prefix + message);
        message = ChatColor.GOLD + " Robbing" + ChatColor.RED + " found an unexpected error.";
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + prefix + message);
        message = " Unexpected error, please contact our staff: https://discord.gg/Hh9zMQnWvW.";
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + prefix + message);
        message = String.format(" ERROR CODE: %s.", code);
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + prefix + message);
        message = " The plugin will continue to work, but we suggest you restart it.";
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + prefix + message);
        message = "====================================================";
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + prefix + message);
    }
}
