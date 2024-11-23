package com.zsinnovations.gamebox.repositories;

import com.zsinnovations.gamebox.R;
import com.zsinnovations.gamebox.models.Game;
import java.util.ArrayList;
import java.util.List;

public class GameRepository {
    private List<Game> games;
    private List<Game> favorites;

    public GameRepository() {
        games = new ArrayList<>();
        favorites = new ArrayList<>();
        loadGames();
    }

    // Load initial list of games
    private void loadGames() {
        games.add(new Game("Game 1", R.drawable.stadia_controller_24px, false));
        games.add(new Game("Game 2", R.drawable.stadia_controller_24px, false));
        games.add(new Game("Game 3", R.drawable.stadia_controller_24px, false));
        // Add more games as needed
    }

    public List<Game> getAllGames() {
        return games;
    }

    public List<Game> getFavorites() {
        return favorites;
    }

    public void addFavorite(Game game) {
        if (!favorites.contains(game)) {
            game.setFavorite(true);
            favorites.add(game);
        }
    }

    public void removeFavorite(Game game) {
        if (favorites.contains(game)) {
            game.setFavorite(false);
            favorites.remove(game);
        }
    }
}
