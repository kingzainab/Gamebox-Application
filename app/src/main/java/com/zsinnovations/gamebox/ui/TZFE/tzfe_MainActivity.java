package com.zsinnovations.gamebox.ui.TZFE;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.zsinnovations.gamebox.R;

public class tzfe_MainActivity extends AppCompatActivity {
    private MediaPlayer backgroundMusic;
    private boolean isMusicPlaying = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize background music
        backgroundMusic = MediaPlayer.create(this, R.raw.tzfe_music);
        backgroundMusic.setLooping(true);
        backgroundMusic.start();

        // Play button
        Button playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gameIntent = new Intent(tzfe_MainActivity.this, tzfe_GameActivity.class);
                startActivity(gameIntent);
            }
        });

        // Volume button
        ImageButton volumeButton = findViewById(R.id.volumeButton);
        volumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMusicPlaying) {
                    backgroundMusic.pause();
                    volumeButton.setImageResource(R.drawable.volume_down);
                } else {
                    backgroundMusic.start();
                    volumeButton.setImageResource(R.drawable.volume_icon);
                }
                isMusicPlaying = !isMusicPlaying;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isMusicPlaying && backgroundMusic != null) {
            backgroundMusic.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (backgroundMusic != null && backgroundMusic.isPlaying()) {
            backgroundMusic.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (backgroundMusic != null) {
            backgroundMusic.release();
            backgroundMusic = null;
        }
    }
}
