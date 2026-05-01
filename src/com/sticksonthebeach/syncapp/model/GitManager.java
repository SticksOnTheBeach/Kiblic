package com.sticksonthebeach.syncapp.model;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Handles Git logic: status detection, staging, commit, and push.
 */
public class GitManager {

    private static final Logger logger = LoggerFactory.getLogger(GitManager.class);

    private final File localRepositoryPath;



    public GitManager(File localRepositoryPath) {
        if (localRepositoryPath == null || !localRepositoryPath.exists()) {
            throw new IllegalArgumentException("Repository path must not be null and must exist.");
        }
        this.localRepositoryPath = localRepositoryPath;
    }

    /**
     * Analyzes the repository and returns all changed, untracked, or deleted files.
     *
     * @return A set of relative file paths with pending changes.
     */
    public Set<String> getChangedFiles() {
        Set<String> changedFiles = new HashSet<>();
        try (Git git = Git.open(localRepositoryPath)) {
            Status status = git.status().call();
            changedFiles.addAll(status.getModified());
            changedFiles.addAll(status.getUntracked());
            changedFiles.addAll(status.getMissing());
        } catch (IOException | GitAPIException e) {
            logger.error("Failed to read repository status: {}", e.getMessage());
        }
        return changedFiles;
    }

    /**
     * Adds a specific file to the staging area.
     *
     * @param filePath The relative path of the file to stage.
     * @return True if staging was successful, false otherwise.
     */
    public boolean stageFile(String filePath) {
        try (Git git = Git.open(localRepositoryPath)) {
            git.add().addFilepattern(filePath).call();
            return true;
        } catch (IOException | GitAPIException e) {
            logger.error("Failed to stage file '{}': {}", filePath, e.getMessage());
            return false;
        }
    }

    /**
     * Commits all currently staged changes with the given message.
     *
     * @param message The commit message.
     * @return True if the commit was successful, false otherwise.
     */
    public boolean commitStagedChanges(String message) {
        try (Git git = Git.open(localRepositoryPath)) {
            git.commit().setMessage(message).call();
            logger.info("Commit successful: {}", message);
            return true;
        } catch (IOException | GitAPIException e) {
            logger.error("Failed to commit staged changes: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Pushes committed changes to the configured remote repository.
     *
     * @return True if the push was successful, false otherwise.
     */
    public boolean pushToRemote() {
        try (Git git = Git.open(localRepositoryPath)) {
            git.push().call();
            logger.info("Push to remote successful.");
            return true;
        } catch (IOException | GitAPIException e) {
            logger.error("Failed to push to remote: {}", e.getMessage());
            return false;
        }
    }
    
    
    /**
     * Switches the current working directory to an existing branch.
     * 
     * @param branchName The name of the branch to checkout.
     * @return True if the checkout was successful.
     */
    public boolean changeBranch(String branchName) {
        if (branchName == null || branchName.isBlank()) {
            logger.error("Branch name cannot be null or empty.");
            return false;
        }

        try (Git git = Git.open(localRepositoryPath)) {
            git.checkout()
               .setName(branchName)
               .call();
            
            logger.info("Successfully switched to branch: {}", branchName);
            return true;
        } catch (IOException | GitAPIException e) {
            logger.error("Failed to checkout branch '{}': {}", branchName, e.getMessage());
            return false;
        }
    }

    /**
     * Creates a new branch in the local repository.
     * Note: This does NOT switch to the new branch automatically.
     * 
     * @param branchName The name of the new branch.
     * @return True if the branch was created successfully.
     */
    public boolean createBranch(String branchName) {
        if (branchName == null || branchName.isBlank()) {
            logger.error("New branch name cannot be null or empty.");
            return false;
        }

        try (Git git = Git.open(localRepositoryPath)) {
            git.branchCreate()
               .setName(branchName)
               .call();
            
            logger.info("Successfully created branch: {}", branchName);
            return true;
        } catch (IOException | GitAPIException e) {
            logger.error("Failed to create branch '{}': {}", branchName, e.getMessage());
            return false;
        }
    }
}