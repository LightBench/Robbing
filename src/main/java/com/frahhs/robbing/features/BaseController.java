package com.frahhs.robbing.features;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.managers.ConfigManager;
import com.frahhs.robbing.managers.MessagesManager;

public abstract class BaseController {
    protected final ConfigManager config = Robbing.getInstance().getConfigManager();
    protected final MessagesManager messages = Robbing.getInstance().getMessagesManager();
}
