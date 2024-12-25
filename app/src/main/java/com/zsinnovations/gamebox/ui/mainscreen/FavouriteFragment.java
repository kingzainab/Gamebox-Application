package com.zsinnovations.gamebox.ui.mainscreen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.zsinnovations.gamebox.R;
import com.zsinnovations.gamebox.adapters.GameAdapter;
import com.zsinnovations.gamebox.ui.TZFE.tzfe_MainActivity;
import com.zsinnovations.gamebox.ui.balloonburst.BG_SplashScreen;
import com.zsinnovations.gamebox.ui.flappybird.FB_SplashScreen;
import com.zsinnovations.gamebox.ui.snakegame.SG_SplashScreen;
import com.zsinnovations.gamebox.ui.tetris.activity.StartActivity;
import com.zsinnovations.gamebox.ui.tictac.TT_SplashScreen;
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
            gridView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
                String gameName = gameNames[position];

                switch (gameName) {
                    case "Flappy Bird":
                        Intent fbIntent = new Intent(requireContext(), FB_SplashScreen.class);
                        startActivity(fbIntent);
                        break;
                    case "Balloon Zap":
                        Intent bzIntent = new Intent(requireContext(), BG_SplashScreen.class);
                        startActivity(bzIntent);
                        break;
                    case "Grow the Snake":
                        Intent gsIntent = new Intent(requireContext(), SG_SplashScreen.class);
                        startActivity(gsIntent);
                        break;
                    case "Tic-Tac-Toe":
                        Intent TtIntent = new Intent(requireContext(), TT_SplashScreen.class);
                        startActivity(TtIntent);
                        break;
                    case "Tetris":
                        Intent TetristIntent = new Intent(requireContext(), StartActivity.class);
                        startActivity(TetristIntent);
                        break;
                    case "2048":
                        Intent TZFEIntent = new Intent(requireContext(), tzfe_MainActivity.class);
                        startActivity(TZFEIntent);
                        break;

                    default:

                        break;
                }
            });
        }
    }

    private int getGameImageResource(String gameName) {
        switch (gameName) {
            case "Flappy Bird":
                return R.drawable.flappybird_hrz;

            case "Balloon Zap":
                return R.drawable.balloon_hrz;
            case "Grow the Snake":
                return R.drawable.snake_hrz;
            case "Tic-Tac-Toe":
                return R.drawable.tictac_hrz;
            case "Tetris":
                return R.drawable.tetris_hrz;
            case "2048":
                return R.drawable.tfze_hrz;
            default:
                return R.drawable.default_game;
        }
    }
}