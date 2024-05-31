package com.frahhs.robbing.util.logging;

import com.google.common.collect.Maps;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.regex.Pattern;

public enum ConsoleColor {
    RESET("[0m"),

    // Regular Colors
    DARK_BLACK("[30m"),
    DARK_RED("[31m"),
    DARK_GREEN("[32m"),
    DARK_YELLOW("[33m"),
    DARK_BLUE("[34m"),
    DARK_PURPLE("[35m"),
    DARK_CYAN("[36m"),
    DARK_WHITE("[37m"),

    // High Intensity
    BLACK("[0;30;1m"),
    RED("[0;31;1m"),
    GREEN("[0;32;1m"),
    YELLOW("[0;33;1m"),
    BLUE("[0;34;1m"),
    PURPLE("[0;35;1m"),
    CYAN("[0;36;1m"),
    WHITE("[0;37;1m"),

    // Bold
    DARK_BLACK_BOLD("[1;30m"),
    DARK_RED_BOLD("[1;31m"),
    DARK_GREEN_BOLD("[1;32m"),
    DARK_YELLOW_BOLD("[1;33m"),
    DARK_BLUE_BOLD("[1;34m"),
    DARK_PURPLE_BOLD("[1;35m"),
    DARK_CYAN_BOLD("[1;36m"),
    DARK_WHITE_BOLD("[1;37m"),

    // Bold High Intensity
    BLACK_BOLD("[1;30;1m"),
    RED_BOLD("[1;31;1m"),
    GREEN_BOLD("[1;32;1m"),
    YELLOW_BOLD("[1;33;1m"),
    BLUE_BOLD("[1;34;1m"),
    PURPLE_BOLD("[1;35;1m"),
    CYAN_BOLD("[1;36;1m"),
    WHITE_BOLD("[1;37;1m"),

    // Underline
    DARK_BLACK_UNDERLINED("[4;30m"),
    DARK_RED_UNDERLINED("[4;31m"),
    DARK_GREEN_UNDERLINED("[4;32m"),
    DARK_YELLOW_UNDERLINED("[4;33m"),
    DARK_BLUE_UNDERLINED("[4;34m"),
    DARK_PURPLE_UNDERLINED("[4;35m"),
    DARK_CYAN_UNDERLINED("[4;36m"),
    DARK_WHITE_UNDERLINED("[4;37m"),

    // Background
    DARK_BLACK_BACKGROUND("[40m"),
    DARK_RED_BACKGROUND("[41m"),
    DARK_GREEN_BACKGROUND("[42m"),
    DARK_YELLOW_BACKGROUND("[43m"),
    DARK_BLUE_BACKGROUND("[44m"),
    DARK_PURPLE_BACKGROUND("[45m"),
    DARK_CYAN_BACKGROUND("[46m"),
    DARK_WHITE_BACKGROUND("[47m"),

    // High Intensity backgrounds
    BLACK_BACKGROUND("[0;100m"),
    RED_BACKGROUND("[0;101m"),
    GREEN_BACKGROUND("[0;102m"),
    YELLOW_BACKGROUND("[0;103m"),
    BLUE_BACKGROUND("[0;104m"),
    PURPLE_BACKGROUND("[0;105m"),
    CYAN_BACKGROUND("[0;106m"),
    WHITE_BACKGROUND("[0;107m");

    public static final char COLOR_CHAR = '\u001B';
    private final String toString;

    ConsoleColor(String code) {
        this.toString = COLOR_CHAR + code;
    }

    @NotNull
    @Override
    public String toString() {
        return toString;
    }
}
