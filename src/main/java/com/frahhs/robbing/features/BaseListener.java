package com.frahhs.robbing.features;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.managers.ConfigManager;
import com.frahhs.robbing.managers.MessagesManager;
import org.bukkit.event.Listener;

public abstract class BaseListener implements Listener {
    protected final ConfigManager configManager = Robbing.getInstance().getConfigManager();;
    protected final MessagesManager messagesManager = Robbing.getInstance().getMessagesManager();
}
