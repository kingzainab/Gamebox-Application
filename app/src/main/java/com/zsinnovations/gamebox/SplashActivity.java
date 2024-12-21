package com.zsinnovations.gamebox;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.zsinnovations.gamebox.utils.MusicManager;
import com.zsinnovations.gamebox.utils.SettingsManager;

public class SplashActivity extends AppCompatActivity {
    private SettingsManager settingsManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        settingsManager = new SettingsManager(this);
        if (!MusicManager.isPlaying()) {
            settingsManager.startMusic();
        }
        Button playButton = findViewById(R.id.play_btn_home);
        playButton.setOnClickListener(v -> {
            if (isMusicEnabled()) {
                settingsManager.resumeMusic();
            }
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
        // Settings Icon
        ImageView settingsIcon = findViewById(R.id.settingsIconSplashActivity);
        settingsIcon.setOnClickListener(v -> settingsManager.showSettingsDialog(this));
    }

    private boolean isMusicEnabled() {
        SharedPreferences prefs = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        return prefs.getBoolean(SettingsManager.PREF_MUSIC_ENABLED, true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        settingsManager.pauseMusic();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isMusicEnabled()) {
            settingsManager.resumeMusic();
        }
    }
}
