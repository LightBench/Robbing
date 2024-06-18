package com.frahhs.robbing.util.logging;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

class FileFormatter extends Formatter {
    @Override
    public String format(LogRecord record) {
        String date_pattern = "[yyyy-MM-dd] [HH:mm:ss.SSS]";
        StringBuilder builder = new StringBuilder();

        Level level = record.getLevel();
        String levelStr = level.getName();

         if(Level.SEVERE.equals(level)) {
            levelStr = "ERROR";
        } else if(Level.FINE.equals(level)) {
            levelStr = "FINE";
        } else if(Level.FINER.equals(level)) {
            levelStr = "FINER";
        } else if(Level.FINEST.equals(level)) {
            levelStr = "FINEST";
        }

         String message = formatMessage(record);

        // Handle fatal logs;
        if(record.getParameters() != null) {
            for (Object parameter : record.getParameters()) {
                if (parameter instanceof FatalCode) {
                    levelStr = "FATAL";
                    message = "Error code: " + parameter;
                }
            }
        }

        builder.append(new SimpleDateFormat(date_pattern).format(new Date(record.getMillis())))
                .append(" ")
                .append("[")
                .append(levelStr)
                .append("] ")
                .append(message)
                .append(System.lineSeparator());


        return builder.toString();
    }
}