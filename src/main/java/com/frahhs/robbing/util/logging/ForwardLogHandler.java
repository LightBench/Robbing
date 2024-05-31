package com.frahhs.robbing.util.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/** Handler for console messages implemented in CraftBukkit:
 * <a href="https://hub.spigotmc.org/stash/projects/SPIGOT/repos/craftbukkit/browse/src/main/java/org/bukkit/craftbukkit/util/ForwardLogHandler.java?at=f41aae401e71f7fe00987da7ac59982016598c00">Reference</a>
 */
public class ForwardLogHandler extends ConsoleHandler {
    private Map<String, Logger> cachedLoggers = new ConcurrentHashMap<String, Logger>();

    private Logger getLogger(String name) {
        Logger logger = cachedLoggers.get(name);
        if (logger == null) {
            logger = LogManager.getLogger(name);
            cachedLoggers.put(name, logger);
        }

        return logger;
    }

    @Override
    public void publish(LogRecord record) {
        Logger logger = getLogger(String.valueOf(record.getLoggerName())); // See SPIGOT-1230
        Throwable exception = record.getThrown();
        Level level = record.getLevel();

        // Changed in Robbing
        // String message = getFormatter().formatMessage(record);

        // Handle fatal logs;
        if(record.getParameters() != null) {
            for (Object parameter : record.getParameters()) {
                if (parameter instanceof FatalCode) {
                    for(String message : getFatalMessages((FatalCode) parameter)) {
                        LogRecord logRecord = new LogRecord(Level.SEVERE, message);
                        message = getFormatter().format(logRecord);
                        logger.error(message, exception);
                    }
                }
                return;
            }
        }

        String message = getFormatter().format(record);

        if(level.intValue() < getLevel().intValue())
            return;

        if (level == Level.SEVERE) {
            logger.error(message, exception);
        } else if (level == Level.WARNING) {
            logger.warn(message, exception);
        } else if (level == Level.INFO) {
            logger.info(message, exception);
        } else if (level == Level.CONFIG) {
            logger.info(message, exception);
        } else if (level == Level.FINE) {
            logger.info(message, exception);
        } else if (level == Level.FINER) {
            logger.info(message, exception);
        } else if (level == Level.FINEST) {
            logger.info(message, exception);
        }
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() throws SecurityException {
    }

    private List<String> getFatalMessages(FatalCode code) {
        List<String> messages = new ArrayList<>();
        messages.add(String.format("%s=====================================================%s", ConsoleColor.DARK_RED, ConsoleColor.RESET));
        messages.add(String.format("%sRobbing%s%s found an unexpected error.%s", ConsoleColor.DARK_YELLOW_BOLD, ConsoleColor.RESET, ConsoleColor.DARK_RED, ConsoleColor.RESET));
        messages.add(String.format("%sUnexpected error, please contact our staff:%s", ConsoleColor.DARK_RED, ConsoleColor.RESET));
        messages.add(String.format("%shttps://discord.gg/Hh9zMQnWvW.%s", ConsoleColor.DARK_RED, ConsoleColor.RESET));
        messages.add(String.format("%sERROR CODE: %s%s%s.%s", ConsoleColor.DARK_RED, ConsoleColor.DARK_CYAN_BOLD, code, ConsoleColor.DARK_RED, ConsoleColor.RESET));
        messages.add(String.format("%sThe plugin will continue to work, but we suggest you%s", ConsoleColor.DARK_RED, ConsoleColor.RESET));
        messages.add(String.format("%sto do a restart.%s", ConsoleColor.DARK_RED, ConsoleColor.RESET));
        messages.add(String.format("%s=====================================================%s", ConsoleColor.DARK_RED, ConsoleColor.RESET));
        return messages;
    }
}