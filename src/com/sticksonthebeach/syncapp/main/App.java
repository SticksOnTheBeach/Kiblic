package com.sticksonthebeach.syncapp.main;



import java.io.File;

import com.google.api.services.drive.Drive;
import com.sticksonthebeach.syncapp.controller.GitController;
import com.sticksonthebeach.syncapp.model.DriveAuthenticator;
import com.sticksonthebeach.syncapp.model.DriveManager;
import com.sticksonthebeach.syncapp.model.GitManager;
import com.sticksonthebeach.syncapp.util.Constants;
import com.sticksonthebeach.syncapp.view.GitPanelView;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Model initilisation
        File mockRepoPath = new File("/Users/mael/Desktop/IUT1/.../Kiblic");
        GitManager gitManager = new GitManager(mockRepoPath);

        // View Initilisation
        GitPanelView gitView = new GitPanelView();
        
        // 1. On engage la sécurité pour obtenir l'accès
        Drive googleService = DriveAuthenticator.getDriveService();

        // 2. On embauche le manager en lui donnant l'accès
        DriveManager driveManager = new DriveManager(googleService);

        // Et voilà, driveManager est prêt à travailler !

        // 3. Initialisation des Contrôleurs (On relie Modèle et Vue)
        new GitController(gitManager, gitView);
        
        
        // 4. Configuration de la fenêtre principale
        Scene scene = new Scene(gitView, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        primaryStage.setTitle(Constants.APP_TITLE);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
