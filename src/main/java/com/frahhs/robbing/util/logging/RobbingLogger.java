package com.frahhs.robbing.util.logging;

import com.frahhs.robbing.util.FileUtil;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.*;
import java.util.stream.Collectors;


/**
 * Provides logging functionalities for the plugin.
 */
public class RobbingLogger {
    private final JavaPlugin plugin;
    private final Logger logger;

    private String prefix = "[Robbing] ";

    private File logDirectory;
    private FileHandler fileHandler;
    private ForwardLogHandler forwardLogHandler;

    private final int MAXIMUM_LOGS_STORED = 30;

    /**
     * Constructs a new RobbingLogger instance.
     *
     * @param plugin The JavaPlugin instance associated with this logger.
     */
    public RobbingLogger(JavaPlugin plugin) {
        this.plugin = plugin;
        this.logger = Logger.getLogger("RobbingLogger");
        this.logger.setLevel(Level.ALL);

        // Setup log files
        createDirectory();
        archiveLatestLog();
        removeOldArchives();

        // Get logger handlers
        this.fileHandler = getFileHandler();
        this.forwardLogHandler = getForwardLogHandler();

        // Logger Settings
        logger.setUseParentHandlers(false);

        // Remove lck files
        removeLck();

        // Add handlers
        logger.addHandler(fileHandler);
        logger.addHandler(forwardLogHandler);
    }

    public void close() {
        fileHandler.close();
        logger.removeHandler(fileHandler);
        fileHandler = null;
        forwardLogHandler.close();
        logger.removeHandler(forwardLogHandler);
        forwardLogHandler = null;
        removeLck();
    }

    /**
     * Retrieves the logging level.
     *
     * @return The logging level.
     */
    public Level getLevel() {
        return logger.getLevel();
    }

    /**
     * Sets the logging level.
     *
     * @param level The logging level to set.
     */
    public void setLevel(Level level) {
        forwardLogHandler.setLevel(level);
        fileHandler.setLevel(Level.ALL);
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

    private void log(Level level, String msg) {
        logger.log(level, msg);
        removeLck();
    }

    private void log(Level level, String msg, Object param) {
        logger.log(level, msg, param);
        removeLck();
    }

    private void log(Level level, String msg, Object[] params) {
        logger.log(level, msg, params);
        removeLck();
    }

    /**
     * Logs a debug message if the logging level is set to ALL.
     *
     * @param message The debug message to log.
     */
    public void config(String message, Object ...args) {
        log(Level.CONFIG, String.format(message, args));
    }

    /**
     * Logs an info message if the logging level is set to ALL or INFO.
     *
     * @param message The info message to log.
     */
    public void info(String message, Object ...args) {
        logger.info(String.format(message, args));
    }

    /**
     * Logs a warning message if the logging level is set to ALL, INFO, or WARNING.
     *
     * @param message The warning message to log.
     */
    public void warning(String message, Object ...args) {
        log(Level.WARNING, String.format(message, args));
    }

    /**
     * Logs an error message if the logging level is set to SEVERE.
     *
     * @param message The error message to log.
     */
    public void error(String message, Object ...args) {
        log(Level.SEVERE, String.format(message, args));
    }

    /**
     * Logs a trace message.
     * <p>
     * TRACE is a message level providing tracing information.
     * <p>
     * All the TRACE, TRACER, and TRACEST are intended for relatively
     * detailed tracing. The exact meaning of the three levels will
     * vary between subsystems, but in general, TRACEST should be used
     * for the most voluminous detailed output, TRACER for somewhat
     * less detailed output, and TRACE for the  lowest volume (and
     * most important) messages.
     * <p>
     * In general the TRACE level should be used for information
     * that will be broadly interesting to developers who do not have
     * a specialized interest in the specific subsystem.
     * <p>
     * FINE messages might include things like minor (recoverable)
     * failures. Issues indicating potential performance problems
     * are also worth logging as FINE.
     *
     * @param message The debug message to log.
     */
    public void trace(String message, Object ...args) {
        log(Level.FINE, String.format(message, args));
    }

    /**
     * Logs a tracer message.
     * <p>
     * TRACER indicates a fairly detailed tracing message.
     * By default, logging calls for entering, returning, or throwing
     * an exception are traced at this level.
     *
     * @param message The debug message to log.
     */
    public void tracer(String message, Object ...args) {
        log(Level.FINER, String.format(message, args));
    }

    /**
     * Logs a tracest message if the logging level is set to FINEST.
     * <p>
     * TRACEST indicates a highly detailed tracing message.
     * This level is initialized to <CODE>300</CODE>.
     * @param message The debug message to log.
     */
    public void tracest(String message, Object ...args) {
        log(Level.FINEST, String.format(message, args));
    }

    /**
     * Logs an unexpected error message with a provided Fatal code.
     *
     * @param code The randomly assigned error code.
     */
    public void fatal(FatalCode code) {
        log(Level.SEVERE, "", code);
    }

    private ForwardLogHandler getForwardLogHandler() {
        forwardLogHandler = new ForwardLogHandler();
        ConsoleFormatter consoleFormatter = new ConsoleFormatter();
        forwardLogHandler.setFormatter(consoleFormatter);
        forwardLogHandler.setLevel(Level.INFO);
        return forwardLogHandler;
    }

    public void createDirectory() {
        logDirectory = Paths.get(plugin.getDataFolder().getPath(), "logs").toFile();

        if(!logDirectory.exists())
            if(!logDirectory.mkdir())
                throw new RuntimeException("Error while creating Robbing logs folder.");

        if(!logDirectory.isDirectory())
            throw new RuntimeException("Robbing logs directory is not a valid directory.");
    }

    private FileHandler getFileHandler() {
        // Add file handler
        FileHandler fileHandler;

        try {
            Path latestLogPath = Paths.get(logDirectory.getPath(), "latest.log");
            fileHandler = new FileHandler(latestLogPath.toString(), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        FileFormatter formatter = new FileFormatter();
        fileHandler.setFormatter(formatter);
        fileHandler.setLevel(Level.ALL);

        return fileHandler;
    }

    private void removeLck() {
        // Get and Remove lck files
        File[] lckFiles = logDirectory.listFiles((dir1, name) -> name.toLowerCase().endsWith(".lck"));
        if(lckFiles != null)
            for(File lckFile : lckFiles)
                if(!lckFile.delete())
                    throw new RuntimeException("Error while removing Robbing logs file.");
    }

    private void archiveLatestLog() {
        Path latestLogPath = Paths.get(logDirectory.getPath(), "latest.log");
        File latestFile = latestLogPath.toFile();

        if(!latestFile.exists())
            return;

        if(FileUtil.isFileEmpty(latestFile))
            return;

        Date nowDate = new Date(System.currentTimeMillis());
        String now = new SimpleDateFormat("yyyy-MM-dd").format(nowDate);

        int index = 0;
        File archive;
        do {
            index++;
            archive = Paths.get(logDirectory.getPath(), now + "-" + index + ".tar.gz").toFile();
        } while (archive.exists());

        // Rename latest log file
        Path namedLogPath = Paths.get(logDirectory.getPath(), now + "-" + index + ".log");
        try {
            Files.move(latestLogPath, namedLogPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Archive file
        FileUtil.toTarGzipFile(namedLogPath.toFile());
    }

    private void removeOldArchives() {
        // Get the list of all files in the folder
        File[] allFiles = logDirectory.listFiles();

        // Check if the folder exists and is a directory
        if (!(allFiles != null && logDirectory.isDirectory()))
            return;

        // Get today's date as a string in the format YYYY-MM-DD
        String todayDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        // Filter files to only include those that match the pattern
        // and are not from today, then sort the filtered list of files
        List<File> filteredFiles = Arrays.stream(allFiles)
                .filter(file -> file.getName().matches("\\d{4}-\\d{2}-\\d{2}-\\d+\\.tar\\.gz"))
                .filter(file -> !file.getName().startsWith(todayDate))
                .sorted((f1, f2) -> {
                    // Extract the file names
                    String name1 = f1.getName();
                    String name2 = f2.getName();

                    // Split the file names into parts
                    String[] parts1 = name1.split("-");
                    String[] parts2 = name2.split("-");

                    // Compare the date parts
                    for (int i = 0; i < 3; i++) {
                        int comparison = parts1[i].compareTo(parts2[i]);
                        if (comparison != 0) {
                            return comparison;
                        }
                    }

                    // Compare the final number part
                    int number1 = Integer.parseInt(parts1[3].split("\\.")[0]);
                    int number2 = Integer.parseInt(parts2[3].split("\\.")[0]);
                    return Integer.compare(number1, number2);
                }).collect(Collectors.toList());

        // Print the sorted list of files
        for (File file : filteredFiles) {
            System.out.println(file.getName());
        }

        while (filteredFiles.size() > MAXIMUM_LOGS_STORED) {
            filteredFiles.get(0).delete();
            filteredFiles.remove(0);
        }
    }
}
