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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

/**
 * Exclusively handles Google OAuth 2.0 authentication.
 * Follows the Single Responsibility Principle.
 */
public class DriveAuthenticator {

    private static final String APPLICATION_NAME = "Kiblic Sync App";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    private DriveAuthenticator() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated.");
    }

    /**
     * Authenticates the user and returns the ready-to-use Drive service.
     *
     * @return An authorized Drive instance.
     * @throws DriveAuthenticationException if authentication fails for any reason.
     */
    public static Drive getDriveService() {
        try {
            final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

            InputStream in = DriveAuthenticator.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
            if (in == null) {
                throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
            }

            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                    httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                    .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                    .setAccessType("offline")
                    .build();

            LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
            Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

            return new Drive.Builder(httpTransport, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME)
                    .build();

        } catch (IOException | GeneralSecurityException e) {
            // Never return null — callers must handle this explicitly.
            throw new DriveAuthenticationException(
                    "Failed to authenticate with Google Drive: " + e.getMessage(), e);
        }
    }
}