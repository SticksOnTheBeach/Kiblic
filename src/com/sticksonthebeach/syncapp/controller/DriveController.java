package com.sticksonthebeach.syncapp.controller;

import com.sticksonthebeach.syncapp.model.DriveManager;
import com.sticksonthebeach.syncapp.view.DrivePanelView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Connects DrivePanelView events to DriveManager operations.
 * Follows the MVC pattern: the controller owns the logic, the view owns the UI.
 */
public class DriveController {

    private static final Logger logger = LoggerFactory.getLogger(DriveController.class);

    private final DriveManager driveManager;
    private final DrivePanelView driveView;

    public DriveController(DriveManager driveManager, DrivePanelView driveView) {
        if (driveManager == null) throw new IllegalArgumentException("DriveManager cannot be null.");
        if (driveView == null)    throw new IllegalArgumentException("DrivePanelView cannot be null.");

        this.driveManager = driveManager;
        this.driveView = driveView;

        // TODO: bind view events to driveManager actions once DrivePanelView has UI components
    }
}