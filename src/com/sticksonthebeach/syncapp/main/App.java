package com.sticksonthebeach.syncapp.main;



import java.io.File;

import com.sticksonthebeach.syncapp.controller.GitController;
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

        // 3. Initialisation des Contrôleurs (On relie Modèle et Vue)
        new GitController(gitManager, gitView);

        // 4. Configuration de la fenêtre principale
        Scene scene = new Scene(gitView, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        primaryStage.setTitle(Constants.APP_TITLE);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
