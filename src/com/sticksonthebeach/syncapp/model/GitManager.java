package com.sticksonthebeach.syncapp.model;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Gère la logique Git avancée : Statut, Staging (Sas), et Commit.
 */
public class GitManager {

    private final File localRepositoryPath;

    public GitManager(File localRepositoryPath) {
        this.localRepositoryPath = localRepositoryPath;
    }

    /**
     *  Analyze the folder and keep in mind all of the changed files or new ones
     */
    public Set<String> getChangedFiles() {
        Set<String> changedFiles = new HashSet<>();
        try (Git git = Git.open(localRepositoryPath)) {
            Status status = git.status().call();
            changedFiles.addAll(status.getModified());  // Les fichiers modifiés
            changedFiles.addAll(status.getUntracked()); // Les fichiers nouvellement créés
            changedFiles.addAll(status.getMissing());   // Les fichiers supprimés
            
        } catch (IOException | GitAPIException e) {
            System.err.println("ERROR : impossible to read and access to the demand ! : " + e.getMessage());
        }
        return changedFiles;
    }

    /**
	*	Add specific files to the preparation stage
     */
    public boolean stageFile(String filePath) {
        try (Git git = Git.open(localRepositoryPath)) {
            git.add().addFilepattern(filePath).call();
            return true;
            
        } catch (IOException | GitAPIException e) {
            System.err.println("Échec de la mise en sas : " + e.getMessage());
            return false;
        }
    }

    /**
     * Validation : commit the staged changes
     */
    public boolean commitStagedChanges(String message) {
        try (Git git = Git.open(localRepositoryPath)) {
            git.commit().setMessage(message).call();
               
            System.out.println("Commit successfully finished : " + message);
            return true;
            
        } catch (IOException | GitAPIException e) {
            System.err.println("ERROR : issue occured with the commit stages : " + e.getMessage());
            return false;
        }
    }
    
    
    public void pushToRemote() {
	}
    
}