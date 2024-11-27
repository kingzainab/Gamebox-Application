package com.zsinnovations.gamebox.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FavoritesManager {
    private static final String PREFS_NAME = "GameBoxPrefs";
    private static final String FAVORITES_KEY_TEMPLATE = "FavoriteGames_%s";
    private static volatile FavoritesManager instance;
    private final SharedPreferences prefs;
    private final Set<String> favoriteGames = new HashSet<>();
    private final String favoritesKey;

    private FavoritesManager(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Generate a device-specific key for favorites
        String deviceId = Settings.Secure.getString(
                context.getContentResolver(), Settings.Secure.ANDROID_ID);
        favoritesKey = String.format(FAVORITES_KEY_TEMPLATE, deviceId);

        // Check for first launch and clear preferences if necessary
        if (prefs.getBoolean("isFirstLaunch", true)) {
            prefs.edit().clear().apply();
            prefs.edit().putBoolean("isFirstLaunch", false).apply();
        }

        loadFavorites();
    }

    public static synchronized FavoritesManager getInstance(Context context) {
        if (instance == null) {
            synchronized (FavoritesManager.class) {
                if (instance == null) {
                    instance = new FavoritesManager(context);
                }
            }
        }
        return instance;
    }

    private void loadFavorites() {
        favoriteGames.clear();
        favoriteGames.addAll(prefs.getStringSet(favoritesKey, new HashSet<>()));
    }

    private void saveFavorites() {
        prefs.edit().putStringSet(favoritesKey, new HashSet<>(favoriteGames)).apply();
    }

    public void addFavorite(String gameName) {
        if (favoriteGames.add(gameName)) {
            saveFavorites();
        }
    }

    public void removeFavorite(String gameName) {
        if (favoriteGames.remove(gameName)) {
            saveFavorites();
        }
    }

    public List<String> getFavoriteGames() {
        return new ArrayList<>(favoriteGames);
    }

    public boolean isFavorite(String gameName) {
        return favoriteGames.contains(gameName);
    }
}
