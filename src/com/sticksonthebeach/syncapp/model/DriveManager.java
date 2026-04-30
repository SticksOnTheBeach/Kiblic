package com.sticksonthebeach.syncapp.model;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

/**
 * Gère l'authentification OAuth 2.0 et les actions sur Google Drive.
 */
public class DriveManager {

    // --- LES CONSTANTES DE SÉCURITÉ ---
    private static final String APPLICATION_NAME = "Kiblic Sync App";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";     
    // On demande un accès total au Drive (pour créer, modifier, supprimer)
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private final Drive driveService;

    /**
     * Constructeur : Initialise la connexion à la création du Manager.
     */
    public DriveManager() throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        
        // 1. On récupère le "Pass VIP" (Credential)
        Credential credential = getCredentials(HTTP_TRANSPORT);
        
        // 2. On instancie officiellement le majordome (driveService)
        this.driveService = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }


    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        InputStream is = DriveManager.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (is == null) {
            throw new FileNotFoundException("Fichier introuvable, à l'adresse : " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(is));
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    /**
     * Crée un nouveau dossier à la racine du Google Drive.
     * 
     * @param folderName Le nom dynamique du dossier que l'on veut créer.
     * @return L'ID unique du dossier créé.
     */
    public String createCustomFolder(String folderName) {
        if (driveService == null) {
            System.err.println("Google Drive not connected.");
            return null;
        }

        File fileMetadata = new File();
        fileMetadata.setName(folderName);
        fileMetadata.setMimeType("application/vnd.google-apps.folder");

        try {
            File createdFolder = driveService.files().create(fileMetadata)
                    .setFields("id, name")
                    .execute();
                    
            System.out.println("Folder successfully created : " + createdFolder.getName() + " (ID: " + createdFolder.getId() + ")");
            return createdFolder.getId();
            
        } catch (IOException e) {
            System.err.println("ERROR : not in the capacity to write the Folder on the Google Drive server ! : " + e.getMessage());
            return null;
        }
    }
}