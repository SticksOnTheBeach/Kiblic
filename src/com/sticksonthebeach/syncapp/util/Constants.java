package com.sticksonthebeach.syncapp.util;

/**
 * Global application constants.
 * Instantiation is blocked to comply with SonarQube utility class conventions.
 */
public final class Constants {

    private Constants() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated.");
    }

    public static final String APP_TITLE = "Kiblic Sync";
    public static final double WINDOW_WIDTH = 1200.0;
    public static final double WINDOW_HEIGHT = 800.0;

    public static final String STYLE_BG_DARK = "-fx-background-color: #2b2b2b;";
    public static final String STYLE_TEXT_WHITE = "-fx-text-fill: white;";
}