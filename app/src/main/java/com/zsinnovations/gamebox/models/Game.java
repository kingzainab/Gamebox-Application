package com.zsinnovations.gamebox.models;

public class Game {
    private String title;
    private int iconResId; // Resource ID for the game's icon
    private boolean isFavorite;

    // Constructor
    public Game(String title, int iconResId, boolean isFavorite) {
        this.title = title;
        this.iconResId = iconResId;
        this.isFavorite = isFavorite;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
