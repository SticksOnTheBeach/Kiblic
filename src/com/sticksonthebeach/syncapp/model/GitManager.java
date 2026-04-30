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
     * 1. LE SCANNER : Analyse le dossier et liste tous les fichiers modifiés ou nouveaux.
     */
    public Set<String> getChangedFiles() {
        Set<String> changedFiles = new HashSet<>();
        try (Git git = Git.open(localRepositoryPath)) {
            Status status = git.status().call();
            changedFiles.addAll(status.getModified());  // Les fichiers modifiés
            changedFiles.addAll(status.getUntracked()); // Les fichiers nouvellement créés
            changedFiles.addAll(status.getMissing());   // Les fichiers supprimés
            
        } catch (IOException | GitAPIException e) {
            System.err.println("Impossible de lire le statut du radar : " + e.getMessage());
        }
        return changedFiles;
    }

    /**
     * 2. LE SAS DE SÉLECTION : Ajoute un fichier spécifique à la zone de préparation.
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
     * 3. LA VALIDATION : Ne commit QUE les fichiers qui ont été mis dans le sas.
     */
    public boolean commitStagedChanges(String message) {
        try (Git git = Git.open(localRepositoryPath)) {
            git.commit().setMessage(message).call();
               
            System.out.println("Commit effectué : " + message);
            return true;
            
        } catch (IOException | GitAPIException e) {
            System.err.println("Erreur lors de la signature du commit : " + e.getMessage());
            return false;
        }
    }
    
    
    public void pushToRemote() {
	}
    
}