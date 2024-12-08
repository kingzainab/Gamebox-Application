package com.zsinnovations.gamebox.ui.snakegame;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
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

    private Animation animation;
    private ImageView sound_max_level,sound_min_level,sound_mute;
    private Button startButton;
    private MediaPlayer mediaPlayer;
    boolean status = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sg_main);  // Set the layout for this activity


        startButton= findViewById(R.id.startButton);
        sound_max_level = findViewById(R.id.level_two_sound);  // Add this line
        sound_min_level = findViewById(R.id.level_one_sound);  // Add this line
        sound_mute = findViewById(R.id.zero_sound);  // Add this line
        animation = AnimationUtils.loadAnimation(SG_MainActivity.this, R.anim.scale_animation);
        startButton.setAnimation(animation);
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showExitConfirmationDialog();
            }
        });
    }


    private void showExitConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SG_MainActivity.this);
        builder.setTitle("Snake Game ");
        builder.setMessage("Are you sure you want to quit the game?");
        builder.setCancelable(false);

        builder.setNegativeButton("Quit", (dialog, which) -> {
            mediaPlayer.stop();
            Intent intent = new Intent(SG_MainActivity.this, MainActivity.class); // Replace with your main activity class
            startActivity(intent);
            finish();
        });

        builder.setPositiveButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.create().show();
    }


    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences preferences = getSharedPreferences("MusicPrefs", MODE_PRIVATE);
        int savedPosition = preferences.getInt("MusicPosition", 0);
        boolean isPlaying = preferences.getBoolean("MusicPlaying", true);

        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(SG_MainActivity.this, R.raw.fb_audio);
            mediaPlayer.setLooping(true);
        }

        mediaPlayer.seekTo(savedPosition);

        if (isPlaying) {
            mediaPlayer.start();
        }

        sound_max_level.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!status) {
                    mediaPlayer.setVolume(0, 0);
                    sound_max_level.setVisibility(View.INVISIBLE);
                    sound_mute.setVisibility(View.VISIBLE);
                    status = true;
                } else {
                    mediaPlayer.setVolume(1, 1);
                    sound_max_level.setVisibility(View.VISIBLE);
                    sound_mute.setVisibility(View.INVISIBLE);
                    status = false;
                }
            }
        });

        sound_mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!status) {
                    mediaPlayer.setVolume(0, 0);
                    sound_max_level.setVisibility(View.INVISIBLE);
                    sound_mute.setVisibility(View.VISIBLE);
                    status = true;
                } else {
                    mediaPlayer.setVolume(1, 1);
                    sound_max_level.setVisibility(View.VISIBLE);
                    sound_mute.setVisibility(View.INVISIBLE);
                    status = false;
                }
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMusicState();
                Intent intent = new Intent(SG_MainActivity.this, SG_GameActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveMusicState();
    }

    private void saveMusicState() {
        if (mediaPlayer != null) {
            SharedPreferences preferences = getSharedPreferences("MusicPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("MusicPosition", mediaPlayer.getCurrentPosition());
            editor.putBoolean("MusicPlaying", mediaPlayer.isPlaying());
            editor.apply();
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}