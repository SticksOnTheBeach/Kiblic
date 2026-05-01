package com.sticksonthebeach.syncapp.controller;

import com.sticksonthebeach.syncapp.model.GitManager;
import com.sticksonthebeach.syncapp.view.GitPanelView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Connects GitPanelView events to GitManager operations.
 * Follows the MVC pattern: the controller owns the logic, the view owns the UI.
 */
public class GitController {

    private static final Logger logger = LoggerFactory.getLogger(GitController.class);

    private final GitManager gitManager;
    private final GitPanelView gitView;

    public GitController(GitManager gitManager, GitPanelView gitView) {
        if (gitManager == null) throw new IllegalArgumentException("GitManager cannot be null.");
        if (gitView == null)    throw new IllegalArgumentException("GitPanelView cannot be null.");

        this.gitManager = gitManager;
        this.gitView = gitView;

        bindEvents();
    }

    /**
     * Wires UI events from the view to the appropriate model actions.
     */
    private void bindEvents() {
        gitView.getCommitButton().setOnAction(event -> handleCommit());
        gitView.getPushButton().setOnAction(event -> handlePush());
    }

    /**
     * Reads the commit message from the view, stages all changed files, and commits.
     */
    private void handleCommit() {
        String message = gitView.getCommitMessageField().getText().trim();

        if (message.isEmpty()) {
            logger.warn("Commit attempted with an empty message — aborting.");
            // TODO: show an alert dialog to the user via the view
            return;
        }

        boolean success = gitManager.getChangedFiles().stream()
                .allMatch(gitManager::stageFile);

        if (success) {
            gitManager.commitStagedChanges(message);
            gitView.getCommitMessageField().clear();
        } else {
            logger.error("One or more files could not be staged. Commit aborted.");
            // TODO: show an alert dialog to the user via the view
        }
    }

    /**
     * Pushes committed changes to the remote repository.
     */
    private void handlePush() {
        boolean success = gitManager.pushToRemote();
        if (!success) {
            logger.error("Push to remote failed.");
            // TODO: show an alert dialog to the user via the view
        }
    }
}