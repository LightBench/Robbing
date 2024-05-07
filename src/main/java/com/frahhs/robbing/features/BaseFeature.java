package com.frahhs.robbing.features;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.managers.ConfigManager;
import com.frahhs.robbing.managers.MessagesManager;
import com.frahhs.robbing.utils.RBLogger;

/**
 * Abstract base class for features in the plugin.
 */
public abstract class BaseFeature {
    /** Configuration manager instance. */
    protected final ConfigManager config = Robbing.getInstance().getConfigManager();

    /** Messages manager instance. */
    protected final MessagesManager messages = Robbing.getInstance().getMessagesManager();

    /** Logger instance. */
    protected final RBLogger logger = Robbing.getInstance().getRBLogger();
}
