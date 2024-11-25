package com.zsinnovations.gamebox.ui.mainscreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.zsinnovations.gamebox.R;

import com.zsinnovations.gamebox.adapters.GameAdapter;

public class GameFragment extends Fragment {

    private final String[] gameNames = {
            "Game 1", "Game 2", "Game 3", "Game 4","Game 5"
    };

    private final int[] gameImages = {
            R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d,R.drawable.e
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_game, container, false);

        // Find the GridView
        GridView gameGridView = rootView.findViewById(R.id.gameGridView);
        if (gameGridView == null) {
            return rootView; // Return early if GridView is not found
        }

        // Verify arrays are valid
        if (gameNames == null || gameImages == null || gameNames.length != gameImages.length) {
            return rootView; // Return early if data is invalid
        }


        // Set up the adapter
        GameAdapter adapter = new GameAdapter(requireContext(), gameNames, gameImages);
        gameGridView.setAdapter(adapter);

        // Handle click events
        gameGridView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            String gameName = gameNames[position];
            Toast.makeText(requireContext(), "Clicked: " + gameName, Toast.LENGTH_SHORT).show();
        });

        return rootView;
    }
}