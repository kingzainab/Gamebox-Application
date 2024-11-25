package com.zsinnovations.gamebox.ui.mainscreen;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.zsinnovations.gamebox.R;
import com.zsinnovations.gamebox.adapters.GameAdapter;
import com.zsinnovations.gamebox.utils.FavoritesManager;

import java.util.List;

public class FavouriteFragment extends Fragment {
    private GridView gridView;
    private TextView emptyView;
    private FavoritesManager favoritesManager;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);

        gridView = view.findViewById(R.id.gameGridView);
        emptyView = view.findViewById(R.id.emptyFavouritesView);
        favoritesManager = FavoritesManager.getInstance(requireContext());

        updateFavoritesList();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateFavoritesList();
    }

    private void updateFavoritesList() {
        List<String> favoriteGames = favoritesManager.getFavoriteGames();

        if (favoriteGames.isEmpty()) {
            gridView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            gridView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);

            String[] gameNames = favoriteGames.toArray(new String[0]);
            int[] gameImages = new int[gameNames.length];

            for (int i = 0; i < gameNames.length; i++) {
                gameImages[i] = getGameImageResource(gameNames[i]);
            }

            GameAdapter adapter = new GameAdapter(requireContext(), gameNames, gameImages);
            gridView.setAdapter(adapter);
        }
    }

    private int getGameImageResource(String gameName) {
        switch (gameName) {
            case "Game 1":
                return R.drawable.flappy_bird;
            case "Game 2":
                return R.drawable.b;
            case "Game 3":
                return R.drawable.c;
            case "Game 4":
                return R.drawable.d;
            case "Game 5":
                return R.drawable.e;
////            case "Puzzle":
////                return R.drawable.apuzzle;
            default:
                return R.drawable.default_game; // Make sure you have a default image
        }
    }
}