package com.sticksonthebeach.syncapp.view;


import com.sticksonthebeach.syncapp.util.Constants;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * La vue dédiée au panneau Git.
 */
public class GitPanelView extends VBox {

    private final TextField commitMessageField;
    private final Button commitButton;
    private final Button pushButton;

    public GitPanelView() {
        // Configuration visuelle du panneau
        this.setStyle(Constants.STYLE_BG_DARK);
        this.setPadding(new Insets(20));
        this.setSpacing(15);
        this.setAlignment(Pos.TOP_CENTER);

        // Initialisation des composants
        Label titleLabel = new Label("Gestionnaire de Version (Git)");
        titleLabel.setStyle(Constants.STYLE_TEXT_WHITE + " -fx-font-size: 18px; -fx-font-weight: bold;");

        this.commitMessageField = new TextField();
        this.commitMessageField.setPromptText("Entrez votre message de commit ici...");

        this.commitButton = new Button("Faire un Commit");
        this.pushButton = new Button("Push vers GitHub");

        // Assemblage
        this.getChildren().addAll(titleLabel, commitMessageField, commitButton, pushButton);
    }

    // Getters pour que le Contrôleur puisse écouter ces éléments (Encapsulation)
    public Button getCommitButton() { return commitButton; }
    public Button getPushButton() { return pushButton; }
    public TextField getCommitMessageField() { return commitMessageField; }
}
