package com.zsinnovations.gamebox;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.zsinnovations.gamebox.utils.SettingsManager;

public class SplashActivity extends AppCompatActivity {

    private SettingsManager settingsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash); // Replace with your splash layout file

        // Initialize SettingsManager
        settingsManager = new SettingsManager(this);
        settingsManager.startMusic();

        // PLAY Button: Navigate to MainActivity
        Button playButton = findViewById(R.id.play_btn_home);
        playButton.setOnClickListener(v -> {
            // Check if music is enabled, and start music if it's not already playing
            if (isMusicEnabled()) {
                settingsManager.startMusic(); // Ensure music starts if enabled
            }

            // Navigate to MainActivity
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Optional: Close SplashActivity
        });

        // Settings Icon: Display settings dialog
        ImageView settingsIcon = findViewById(R.id.settingsIconSplashActivity);
        settingsIcon.setOnClickListener(v -> settingsManager.showSettingsDialog(this));
    }

    // Check if music is enabled
    private boolean isMusicEnabled() {
        SharedPreferences prefs = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        return prefs.getBoolean(SettingsManager.PREF_MUSIC_ENABLED, true); // Default to true if not set
    }


    @Override
    protected void onPause() {
        super.onPause();
        settingsManager.pauseMusic(); // Pause music if any
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isMusicEnabled()) {
            settingsManager.resumeMusic(); // Resume music if enabled
        }
    }
}
