package com.sticksonthebeach.syncapp.util;

/**
 * Defines the standard MIME types used by Google Drive API.
 * Prevents magic strings and typo-related bugs.
 */
public enum GoogleMimeType {

    // --- Standard Formats ---
    FOLDER("application/vnd.google-apps.folder"),
    TEXT("text/plain"),
    CSV("text/csv"),
    JPEG("image/jpeg"),
    PDF("application/pdf"),
    GENERIC_FILE("application/octet-stream"),
    UNKNOWN("application/vnd.google-apps.unknown"),

    // --- Native Google Workspace Formats (used for conversion) ---
    GOOGLE_DOCS("application/vnd.google-apps.document"),
    GOOGLE_SHEETS("application/vnd.google-apps.spreadsheet");

    private final String value;

    GoogleMimeType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}