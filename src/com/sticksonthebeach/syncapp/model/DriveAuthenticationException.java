package com.sticksonthebeach.syncapp.model;

/**
 * Thrown when authentication with Google Drive fails.
 * Replaces silent null returns from DriveAuthenticator.
 */
public class DriveAuthenticationException extends RuntimeException {

    public DriveAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}