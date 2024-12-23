package com.zsinnovations.gamebox.ui.mainscreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.zsinnovations.gamebox.R;

import com.zsinnovations.gamebox.adapters.GameAdapter;
import com.zsinnovations.gamebox.ui.balloonburst.BG_SplashScreen;
import com.zsinnovations.gamebox.ui.flappybird.FB_SplashScreen;
import com.zsinnovations.gamebox.ui.snakegame.SG_SplashScreen;
import com.zsinnovations.gamebox.ui.tictac.TT_SplashScreen;

public class GameFragment extends Fragment {

    private final String[] gameNames = {
            "Flappy Bird", "Balloon Zap", "Grow the Snake", "Tic-Tac-Toe","Game 5"
    };

    private final int[] gameImages = {
            R.drawable.flappy_bird_logo, R.drawable.balloon_icon, R.drawable.snake_game_icon, R.drawable.tic_tac_logo,R.drawable.e
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_game, container, false);

        // Find the GridView
        GridView gameGridView = rootView.findViewById(R.id.gameGridView);
        if (gameGridView == null) {
            return rootView;
        }

        // Verify arrays are valid
        if (gameNames == null || gameImages == null || gameNames.length != gameImages.length) {
            return rootView;
        }


        // Set up the adapter
        GameAdapter adapter = new GameAdapter(requireContext(), gameNames, gameImages);
        gameGridView.setAdapter(adapter);


        gameGridView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            String gameName = gameNames[position];
            switch (position) {
                case 0:

                    Intent intentGame1 = new Intent(requireContext(), FB_SplashScreen.class);
                    startActivity(intentGame1);
                    break;
                case 1:

                    Intent newIntent = new Intent(requireContext(), BG_SplashScreen.class);
                    startActivity(newIntent);
                    break;
                case 2:
                    Intent snakeIntent = new Intent(requireContext(), SG_SplashScreen.class);
                    startActivity(snakeIntent);
                    break;
                case 3:
                    Intent TtIntent = new Intent(requireContext(), TT_SplashScreen.class);
                    startActivity(TtIntent);
                default:

                    break;
            }
        });

        return rootView;
    }
}