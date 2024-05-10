package com.frahhs.robbing.features;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.providers.ConfigProvider;
import com.frahhs.robbing.providers.MessagesProvider;
import com.frahhs.robbing.utils.RobbingLogger;

/**
 * Abstract base class for features in the plugin.
 */
public abstract class BaseFeature {
    /** Configuration manager instance. */
    protected final ConfigProvider config = Robbing.getInstance().getConfigProvider();

    /** Messages manager instance. */
    protected final MessagesProvider messages = Robbing.getInstance().getMessagesProvider();

    /** Logger instance. */
    protected final RobbingLogger logger = Robbing.getInstance().getRBLogger();
}
