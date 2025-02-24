package controller.MainController;

import atlantafx.base.theme.*;
import javafx.application.Application;
import javafx.scene.Scene;

import java.util.HashMap;
import java.util.Map;

public class ThemeManager {
    private static ThemeManager instance;
    private final Map<String, Theme> themes;
    private Scene currentScene;
    private String currentThemeName;

    private ThemeManager() {
        themes = new HashMap<>();
        // Register available themes
        registerThemes();
    }

    public static ThemeManager getInstance() {
        if (instance == null) {
            instance = new ThemeManager();
        }
        return instance;
    }

    private void registerThemes() {
        // Add available themes
        themes.put("Cupertino Dark", new CupertinoDark());
        themes.put("Cupertino Light", new CupertinoLight());
        themes.put("Nord Dark", new NordDark());
        themes.put("Nord Light", new NordLight());
        themes.put("Primer Dark", new PrimerDark());
        themes.put("Primer Light", new PrimerLight());
    }

    public void setScene(Scene scene) {
        this.currentScene = scene;
    }

    public void setTheme(String themeName) {
        if (!themes.containsKey(themeName)) {
            throw new IllegalArgumentException("Theme not found: " + themeName);
        }

        Theme theme = themes.get(themeName);
        Application.setUserAgentStylesheet(theme.getUserAgentStylesheet());
        currentThemeName = themeName;
    }

    public String getCurrentTheme() {
        return currentThemeName;
    }

    public String[] getAvailableThemes() {
        return themes.keySet().toArray(new String[0]);
    }
}