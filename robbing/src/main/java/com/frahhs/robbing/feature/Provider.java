package com.frahhs.robbing.feature;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.RobbingObject;
import com.frahhs.robbing.util.bag.BagManager;

import java.sql.Connection;

/**
 * Base class for feature providers.
 * Provides access to the database connection and bag manager.
 */
public class Provider extends RobbingObject {
    /** The database connection. */
    protected final Connection dbConnection = Robbing.getInstance().getRobbingDatabase().getConnection();

    /** The bag manager for managing data bags. */
    protected final BagManager bagManager = Robbing.getInstance().getBagManager();
}
