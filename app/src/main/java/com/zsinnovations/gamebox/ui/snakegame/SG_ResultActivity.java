package com.zsinnovations.gamebox.ui.snakegame;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.zsinnovations.gamebox.MainActivity;
import com.zsinnovations.gamebox.R;

public class SG_ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sg_result);

        // Retrieve game results
        int currentScore = getIntent().getIntExtra("SCORE", 0);
        int highestScore = getIntent().getIntExtra("HIGHEST_SCORE", 0);

        // Initialize UI components
        TextView currentScoreTV = findViewById(R.id.currentScoreTextView);
        TextView highestScoreTV = findViewById(R.id.highestScoreTextView);
        Button restartButton = findViewById(R.id.restartButton);
        Button mainMenuButton = findViewById(R.id.mainMenuButton);

        // Set score texts
        currentScoreTV.setText("Your Score: " + currentScore);
        highestScoreTV.setText("Highest Score: " + highestScore);

        // Restart game
        restartButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, SG_GameActivity.class);
            startActivity(intent);
            finish();
        });

        // Return to main menu
        mainMenuButton.setOnClickListener(v -> {
            // Replace with your main menu activity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}