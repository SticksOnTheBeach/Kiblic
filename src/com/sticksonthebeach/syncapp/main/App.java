package com.sticksonthebeach.syncapp.main;

import java.io.File;

import com.google.api.services.drive.Drive;
import com.sticksonthebeach.syncapp.controller.DriveController;
import com.sticksonthebeach.syncapp.controller.GitController;
import com.sticksonthebeach.syncapp.model.DriveAuthenticationException;
import com.sticksonthebeach.syncapp.model.DriveAuthenticator;
import com.sticksonthebeach.syncapp.model.DriveManager;
import com.sticksonthebeach.syncapp.model.GitManager;
import com.sticksonthebeach.syncapp.util.Constants;
import com.sticksonthebeach.syncapp.view.DrivePanelView;
import com.sticksonthebeach.syncapp.view.GitPanelView;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App extends Application {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    @Override
    public void start(Stage primaryStage) {

        // --- Git setup ---
        // TODO: replace this hardcoded path with a config file or user selection dialog
        File repoPath = new File(System.getProperty("app.repoPath", System.getProperty("user.home")));
        GitManager gitManager = new GitManager(repoPath);
        GitPanelView gitView = new GitPanelView();
        new GitController(gitManager, gitView);

        // --- Drive setup ---
        DrivePanelView driveView = new DrivePanelView();
        try {
            Drive googleService = DriveAuthenticator.getDriveService();
            DriveManager driveManager = new DriveManager(googleService);
            new DriveController(driveManager, driveView);
        } catch (DriveAuthenticationException e) {
            // Non-fatal: the app can still run in Git-only mode
            logger.error("Google Drive authentication failed. Drive features will be unavailable.", e);
            // TODO: show a warning banner in the UI
        }

        // --- Main window ---
        Scene scene = new Scene(gitView, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        primaryStage.setTitle(Constants.APP_TITLE);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}