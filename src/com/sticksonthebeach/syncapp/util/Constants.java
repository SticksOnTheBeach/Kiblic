package com.sticksonthebeach.syncapp.util;

/**
 * Classe utilitaire contenant les constantes globales.
 * L'instanciation est bloquée pour respecter les conventions SonarQube.
 */
public final class Constants {

    private Constants() {
        throw new UnsupportedOperationException("Cette classe utilitaire ne peut pas être instanciée.");
    }

    public static final String APP_TITLE = "Kiblic Sync - Édition";
    public static final double WINDOW_WIDTH = 1200.0;
    public static final double WINDOW_HEIGHT = 800.0;
    public static final String STYLE_BG_DARK = "-fx-background-color: #2b2b2b;";
    public static final String STYLE_TEXT_WHITE = "-fx-text-fill: white;";
}