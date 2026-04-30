package com.sticksonthebeach.syncapp.util;

/**
 * Defines the standard MIME types used by Google Drive API.
 * Prevents magic strings and typo-related bugs.
 */
public enum GoogleMimeType {
    
    FOLDER("application/vnd.google-apps.folder"),
    TEXT("text/plain"),
    JPEG("image/jpeg"),
    PDF("application/pdf"),
    GENERIC_FILE("application/octet-stream"); // Fallback for unknown files

    private final String value;

    GoogleMimeType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}