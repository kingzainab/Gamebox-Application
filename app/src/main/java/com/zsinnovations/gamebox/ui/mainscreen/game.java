package com.zsinnovations.gamebox.ui.mainscreen;

import android.os.Bundle;
import android.util.Log;
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

public class game extends Fragment {
    private static final String TAG = "GameFragment";

    private final String[] gameNames = {
            "Game 1", "Game 2", "Game 3", "Game 4"
    };

    private final int[] gameImages = {
            R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView called");
        View rootView = inflater.inflate(R.layout.fragment_game, container, false);

        try {
            // Find the GridView
            GridView gameGridView = rootView.findViewById(R.id.gameGridView);
            if (gameGridView == null) {
                Log.e(TAG, "GridView not found in layout");
                return rootView;
            }

            // Verify arrays are non-null and same length
            if (gameNames == null || gameImages == null || gameNames.length != gameImages.length) {
                Log.e(TAG, "Invalid game data arrays");
                return rootView;
            }

            // Set up the adapter
            GameAdapter adapter = new GameAdapter(requireContext(), gameNames, gameImages);
            gameGridView.setAdapter(adapter);

            // Handle click events
            gameGridView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
                String gameName = gameNames[position];
                Toast.makeText(requireContext(), "Clicked: " + gameName, Toast.LENGTH_SHORT).show();
            });

            Log.d(TAG, "GridView setup completed successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error setting up GridView", e);
        }

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated called");
    }
}