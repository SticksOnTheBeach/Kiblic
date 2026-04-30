package com.sticksonthebeach.syncapp.model;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.sticksonthebeach.syncapp.util.GoogleMimeType;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Optional;

/**
 * Handles Google Drive operations (Upload, Folders, Search).
 * Strictly adheres to Clean Code standards, using Optional and NIO.2.
 */
public class DriveManager {

    private final Drive driveService;

    /**
     * Dependency Injection of the authenticated Drive service.
     * 
     * @param driveService A fully authenticated Google Drive instance.
     * @throws IllegalArgumentException if the provided service is null.
     */
    public DriveManager(Drive driveService) {
        if (driveService == null) {
            throw new IllegalArgumentException("Drive service cannot be null.");
        }
        this.driveService = driveService;
    }

    /**
     * Creates a new folder at the root of Google Drive.
     * 
     * @param folderName The exact name of the folder to create.
     * @return An Optional containing the Folder ID if successful, or empty if it fails.
     */
    public Optional<String> createFolder(String folderName) {
        File fileMetadata = new File();
        fileMetadata.setName(folderName);
        fileMetadata.setMimeType(GoogleMimeType.FOLDER.getValue());

        try {
            File createdFolder = driveService.files().create(fileMetadata)
                    .setFields("id, name")
                    .execute();
            
            // Note: In a official app, use a Logger (like SLF4J) instead of System.out
            // TODO : change the logger !!!
            System.out.println("Success: Folder created with ID " + createdFolder.getId());
            return Optional.of(createdFolder.getId());
            
        } catch (IOException e) {
            System.err.println("API Error while creating folder: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Uploads a local physical file to a specific Google Drive folder.
     * 
     * @param targetFolderId The Google Drive ID of the destination folder.
     * @param localFilePath  The modern NIO.2 Path of the file to upload.
     * @param mimeType       The strictly typed MIME type from our Enum.
     * @return An Optional containing the uploaded File ID if successful, or empty if it fails.
     */
    public Optional<String> uploadFile(String targetFolderId, Path localFilePath, GoogleMimeType mimeType) {
        java.io.File physicalFile = localFilePath.toFile();

        File fileMetadata = new File();
        fileMetadata.setName(physicalFile.getName());
        fileMetadata.setParents(Collections.singletonList(targetFolderId));

        FileContent mediaContent = new FileContent(mimeType.getValue(), physicalFile);

        try {
            File uploadedFile = driveService.files().create(fileMetadata, mediaContent)
                    .setFields("id, name")
                    .execute();
                    
            System.out.println("Success: Uploaded " + uploadedFile.getName());
            return Optional.of(uploadedFile.getId());
            
        } catch (IOException e) {
            System.err.println("API Error while uploading file: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Searches for a folder by its exact name.
     * 
     * @param folderName The name of the folder.
     * @return An Optional containing the Folder ID, or empty if not found.
     */
    public Optional<String> getFolderIdByName(String folderName) {
        try {
            String query = String.format("mimeType='%s' and name='%s' and trashed=false", 
                                         GoogleMimeType.FOLDER.getValue(), folderName);

            FileList result = driveService.files().list()
                    .setQ(query)
                    .setSpaces("drive")
                    .setFields("files(id, name)")
                    .execute();

            if (result.getFiles() != null && !result.getFiles().isEmpty()) {
                return Optional.of(result.getFiles().get(0).getId());
            }

            return Optional.empty();

        } catch (IOException e) {
            System.err.println("API Error while searching for folder: " + e.getMessage());
            return Optional.empty();
        }
    }
    
    
    /**
     * Searches for a generic file (excluding folders) by its exact name.
     * 
     * @param fileName The exact name of the file to search for.
     * @return An Optional containing the File ID, or empty if not found.
     */
    public Optional<String> getFileIdByName(String fileName) {
        try {
            String query = String.format("name='%s' and mimeType != '%s' and trashed=false",fileName, GoogleMimeType.FOLDER.getValue());
            com.google.api.services.drive.model.FileList result = driveService.files().list()
                    .setQ(query)
                    .setSpaces("drive")
                    .setFields("files(id, name)")
                    .execute();

            if (result.getFiles() != null && !result.getFiles().isEmpty()) {
                String fileId = result.getFiles().get(0).getId();
                System.out.println("Success: Found file '" + fileName + "' with ID: " + fileId);
                return Optional.of(fileId);
            }

            System.out.println("Notice: File '" + fileName + "' was not found on the Drive.");
            return Optional.empty();

        } catch (IOException e) {
            System.err.println("API Error while searching for file: " + e.getMessage());
            return Optional.empty();
        }
    }
    
    
    /**
     * Moves a file from its current folder(s) to a new designated folder.
     * 
     * @param fileId   The exact Google Drive ID of the file to move.
     * @param folderId The exact Google Drive ID of the destination folder.
     * @return True if the move was successful, false otherwise.
     */
    public boolean moveFileToFolder(String fileId, String folderId) {
        try {
            File file = driveService.files().get(fileId)
                    .setFields("parents")
                    .execute();

            if (file.getParents() == null || file.getParents().isEmpty()) {
                System.err.println("Notice: File has no parents. It might already be in the root directory.");
                return false;
            }
            String previousParents = String.join(",", file.getParents());
            driveService.files().update(fileId, null)
                    .setAddParents(folderId)
                    .setRemoveParents(previousParents)
                    .setFields("id, parents")
                    .execute();

            System.out.println("Success: File (ID: " + fileId + ") seamlessly moved to folder (ID: " + folderId + ").");
            return true;

        } catch (IOException e) {
            System.err.println("API Error while attempting to move the file: " + e.getMessage());
            return false;
        }
    }
}