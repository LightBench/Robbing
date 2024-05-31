package com.frahhs.robbing.util.logging;

import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

class ConsoleFormatter extends Formatter {

    @Override
    public String format(LogRecord record) {
        StringBuilder builder = new StringBuilder();

        String message = formatMessage(record);

        Level level = record.getLevel();
        if(Level.WARNING.equals(level)) {
            builder .append(ConsoleColor.YELLOW)
                    .append("[Robbing] ");
        } else if(Level.SEVERE.equals(level)) {
            builder .append(ConsoleColor.DARK_RED)
                    .append("[Robbing] ");
        } else if(Level.CONFIG.equals(level)) {
            builder .append(ConsoleColor.DARK_BLUE)
                    .append("[Robbing] ")
                    .append("[CONFIG] ");
        } else if(Level.FINE.equals(level)) {
            builder .append(ConsoleColor.BLUE)
                    .append("[Robbing] ")
                    .append("[TRACE] ");
        } else if(Level.FINER.equals(level)) {
            builder .append(ConsoleColor.BLUE)
                    .append("[Robbing] ")
                    .append("[TRACER] ");
        } else if(Level.FINEST.equals(level)) {
            builder .append(ConsoleColor.BLUE)
                    .append("[Robbing] ")
                    .append("[TRACEST] ");
        } else {
            builder .append("[Robbing] ");
        }

        builder .append(message)
                .append(ConsoleColor.RESET);

        return builder.toString();
    }
}