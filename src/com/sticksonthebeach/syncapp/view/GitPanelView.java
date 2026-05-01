package com.sticksonthebeach.syncapp.view;

import com.sticksonthebeach.syncapp.util.Constants;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * View dedicated to the Git panel.
 * Exposes UI components via getters so the controller can bind events without
 * breaking encapsulation.
 */
public class GitPanelView extends VBox {

    private final TextField commitMessageField;
    private final Button commitButton;
    private final Button pushButton;

    public GitPanelView() {
        this.setStyle(Constants.STYLE_BG_DARK);
        this.setPadding(new Insets(20));
        this.setSpacing(15);
        this.setAlignment(Pos.TOP_CENTER);

        Label titleLabel = new Label("Version Manager (Git)");
        titleLabel.setStyle(Constants.STYLE_TEXT_WHITE + " -fx-font-size: 18px; -fx-font-weight: bold;");

        this.commitMessageField = new TextField();
        this.commitMessageField.setPromptText("Enter your commit message here...");

        this.commitButton = new Button("Commit");
        this.pushButton = new Button("Push to GitHub");

        this.getChildren().addAll(titleLabel, commitMessageField, commitButton, pushButton);
    }

    public Button getCommitButton()          { return commitButton; }
    public Button getPushButton()            { return pushButton; }
    public TextField getCommitMessageField() { return commitMessageField; }
}