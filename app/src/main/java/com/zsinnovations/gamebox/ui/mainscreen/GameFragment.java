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
import com.zsinnovations.gamebox.ui.TZFE.tzfe_MainActivity;
import com.zsinnovations.gamebox.ui.balloonburst.BG_SplashScreen;
import com.zsinnovations.gamebox.ui.flappybird.FB_SplashScreen;
import com.zsinnovations.gamebox.ui.snakegame.SG_SplashScreen;
import com.zsinnovations.gamebox.ui.tetris.activity.StartActivity;
import com.zsinnovations.gamebox.ui.tetris.activity.Tetris_MainActivity;
import com.zsinnovations.gamebox.ui.tictac.TT_SplashScreen;

public class GameFragment extends Fragment {

    private final String[] gameNames = {
            "Flappy Bird","Tetris", "Grow the Snake", "Balloon Zap","2048","Tic-Tac-Toe"
    };

    private final int[] gameImages = {
            R.drawable.flappy_bird_logo, R.drawable.tetris, R.drawable.snake_logo,R.drawable.balloon_icon,R.drawable.tfze_logo,R.drawable.tic_tac_logo
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
                    break;
                case 4:
                    Intent TetristIntent = new Intent(requireContext(), StartActivity.class);
                    startActivity(TetristIntent);
                    break;
                case 5:
                    Intent TZFEIntent = new Intent(requireContext(), tzfe_MainActivity.class);
                    startActivity(TZFEIntent);
                    break;
                default:

                    break;
            }
        });

        return rootView;
    }
}