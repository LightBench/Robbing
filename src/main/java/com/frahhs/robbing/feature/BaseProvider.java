package com.frahhs.robbing.feature;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.bag.BagManager;

import java.sql.Connection;

public class BaseProvider extends Base {
    protected final Connection dbConnection = Robbing.getInstance().getRBDatabase().getConnection();

    protected final BagManager bagManager = Robbing.getInstance().getBagManager();
}
