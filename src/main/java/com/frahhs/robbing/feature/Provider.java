package com.frahhs.robbing.feature;

import com.frahhs.robbing.RBObject;
import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.util.bag.BagManager;

import java.sql.Connection;

public class Provider extends RBObject {
    protected final Connection dbConnection = Robbing.getInstance().getRBDatabase().getConnection();

    protected final BagManager bagManager = Robbing.getInstance().getBagManager();
}
