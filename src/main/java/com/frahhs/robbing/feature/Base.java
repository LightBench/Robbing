package com.frahhs.robbing.feature;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.provider.ConfigProvider;
import com.frahhs.robbing.provider.MessagesProvider;
import com.frahhs.robbing.util.RobbingLogger;

/**
 * Abstract base class for features in the plugin.
 */
public abstract class Base {
    protected final Robbing plugin = Robbing.getInstance();

    /** Configuration manager instance. */
    protected final ConfigProvider config = plugin.getConfigProvider();

    /** Messages manager instance. */
    protected final MessagesProvider messages = plugin.getMessagesProvider();

    /** Logger instance. */
    protected final RobbingLogger logger = plugin.getRBLogger();
}
