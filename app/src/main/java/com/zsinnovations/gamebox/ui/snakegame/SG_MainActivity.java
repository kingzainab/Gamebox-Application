package com.zsinnovations.gamebox.ui.snakegame;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.zsinnovations.gamebox.MainActivity;
import com.zsinnovations.gamebox.R;
import com.zsinnovations.gamebox.ui.balloonburst.BG_GameActivity;
import com.zsinnovations.gamebox.ui.balloonburst.BG_MainActivity;

public class SG_MainActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "SnakeGamePrefs";
    private static final String KEY_VOLUME_STATE = "VolumeState";
    private static final String KEY_MUSIC_POSITION = "MusicPosition";
    private static final String KEY_MUSIC_PLAYING = "MusicPlaying";

    private Animation animation;
    private ImageView sound_max_level, sound_min_level, sound_mute;
    private Button startButton;
    private MediaPlayer mediaPlayer;
    private boolean status = false;
    private SharedPreferences preferences;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
        );
        setContentView(R.layout.activity_sg_main);

        // Initialize SharedPreferences
        preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        status = preferences.getBoolean(KEY_VOLUME_STATE, false);

        initializeViews();
        setupBackPress();
        initializeMediaPlayer();
        updateSoundIconsBasedOnState();
    }

    private void initializeViews() {
        startButton = findViewById(R.id.startButton);
        sound_max_level = findViewById(R.id.level_two_sound);
        sound_min_level = findViewById(R.id.level_one_sound);
        sound_mute = findViewById(R.id.zero_sound);

        animation = AnimationUtils.loadAnimation(this, R.anim.scale_animation);
        startButton.setAnimation(animation);

        setupClickListeners();
    }

    private void initializeMediaPlayer() {
        try {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(this, R.raw.fb_audio);
                if (mediaPlayer != null) {
                    mediaPlayer.setLooping(true);
                    // Restore previous position
                    int savedPosition = preferences.getInt(KEY_MUSIC_POSITION, 0);
                    mediaPlayer.seekTo(savedPosition);

                    // Set volume based on saved state
                    float volume = status ? 0.0f : 1.0f;
                    mediaPlayer.setVolume(volume, volume);

                    // Start playing if it was playing before
                    if (preferences.getBoolean(KEY_MUSIC_PLAYING, true)) {
                        mediaPlayer.start();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupClickListeners() {
        View.OnClickListener soundClickListener = v -> toggleSound();
        sound_max_level.setOnClickListener(soundClickListener);
        sound_mute.setOnClickListener(soundClickListener);

        startButton.setOnClickListener(v -> {
            saveMusicState();
            Intent intent = new Intent(SG_MainActivity.this, SG_GameActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });
    }

    private void toggleSound() {
        if (mediaPlayer != null) {
            status = !status;
            float volume = status ? 0.0f : 1.0f;
            mediaPlayer.setVolume(volume, volume);

            // Update UI
            sound_max_level.setVisibility(status ? View.INVISIBLE : View.VISIBLE);
            sound_mute.setVisibility(status ? View.VISIBLE : View.INVISIBLE);

            // Save state
            preferences.edit()
                    .putBoolean(KEY_VOLUME_STATE, status)
                    .apply();
        }
    }

    private void updateSoundIconsBasedOnState() {
        sound_max_level.setVisibility(status ? View.INVISIBLE : View.VISIBLE);
        sound_mute.setVisibility(status ? View.VISIBLE : View.INVISIBLE);
    }

    private void setupBackPress() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showExitConfirmationDialog();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mediaPlayer == null) {
            initializeMediaPlayer();
        } else if (!mediaPlayer.isPlaying() &&
                preferences.getBoolean(KEY_MUSIC_PLAYING, true)) {
            mediaPlayer.start();
        }
    }

    private void saveMusicState() {
        if (mediaPlayer != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(KEY_MUSIC_POSITION, mediaPlayer.getCurrentPosition());
            editor.putBoolean(KEY_MUSIC_PLAYING, mediaPlayer.isPlaying());
            editor.apply();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            saveMusicState();
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            saveMusicState();
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            saveMusicState();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void showExitConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Snake Game");
        builder.setMessage("Are you sure you want to quit the game?");
        builder.setCancelable(false);

        builder.setNegativeButton("Quit", (dialog, which) -> {
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                mediaPlayer.release();
                mediaPlayer = null;
            }
            finish();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        builder.setPositiveButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.create().show();
    }
}