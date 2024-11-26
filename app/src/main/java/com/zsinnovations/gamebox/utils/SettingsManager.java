package com.zsinnovations.gamebox.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.slider.Slider;
import com.zsinnovations.gamebox.R;
import com.zsinnovations.gamebox.utils.MusicManager;

public class SettingsManager {
    public static final String PREF_MUSIC_VOLUME = "musicVolume";
    public static final String PREF_MUSIC_ENABLED = "musicEnabled";
    private MusicManager musicManager;
    private Context context;

    public SettingsManager(Context context) {
        this.context = context;
        this.musicManager = new MusicManager();
    }

    // Method to start music
    public void startMusic() {
        SharedPreferences prefs = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        // Check if the music is enabled, and start the music if true
        boolean isMusicEnabled = prefs.getBoolean(PREF_MUSIC_ENABLED, true);
        if (isMusicEnabled) {
            // Load saved volume and set it
            int savedVolume = prefs.getInt(PREF_MUSIC_VOLUME, 50); // Default volume 50
            musicManager.setVolume(savedVolume / 100f); // Set volume (0.0f to 1.0f)
            musicManager.startMusic(context); // Start music
        }
    }

    // Method to stop music
    public void stopMusic() {
        musicManager.stopMusic();
    }

    // Method to show settings dialog
    public void showSettingsDialog(AppCompatActivity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View dialogView = activity.getLayoutInflater().inflate(R.layout.dialog_settings, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Initialize components
        Switch toggleMusicSwitch = dialogView.findViewById(R.id.musicToggleButton);
        Slider volumeSlider = dialogView.findViewById(R.id.volumeSlider);
        TextView volumeLabel = dialogView.findViewById(R.id.volumeLabel);

        SharedPreferences prefs = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);

        // Load saved music state
        boolean isMusicEnabled = prefs.getBoolean(PREF_MUSIC_ENABLED, true);
        toggleMusicSwitch.setChecked(isMusicEnabled);

        // Customize switch color based on state
        int enabledColor = Color.parseColor("#15BDA3"); // Green for enabled
        int disabledColor = Color.parseColor("#9E9E9E"); // Gray for disabled

        // Update the switch thumb color based on the initial state
        toggleMusicSwitch.setTrackTintList(ColorStateList.valueOf(isMusicEnabled ? enabledColor : disabledColor));
        toggleMusicSwitch.setThumbTintList(ColorStateList.valueOf(isMusicEnabled ? enabledColor : disabledColor));

        // Load saved volume level
        int savedVolume = prefs.getInt(PREF_MUSIC_VOLUME, 50);
        volumeSlider.setValue(savedVolume);
        volumeLabel.setText("Music Volume: " + savedVolume + "%");

        // Set up Switch listener
        toggleMusicSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(PREF_MUSIC_ENABLED, isChecked);
            editor.apply();

            // Update switch colors
            toggleMusicSwitch.setTrackTintList(ColorStateList.valueOf(isChecked ? enabledColor : disabledColor));
            toggleMusicSwitch.setThumbTintList(ColorStateList.valueOf(isChecked ? enabledColor : disabledColor));

            if (isChecked) {
                musicManager.startMusic(context); // Start music
            } else {
                musicManager.pauseMusic(); // Pause music
            }
        });

        // Set up Slider listener
        volumeSlider.addOnChangeListener((slider, value, fromUser) -> {
            int volume = (int) value;
            volumeLabel.setText("Music Volume: " + volume + "%");
            musicManager.setVolume(volume / 100f);

            if (fromUser) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt(PREF_MUSIC_VOLUME, volume);  // Store as integer
                editor.apply();
            }
        });

        dialogView.findViewById(R.id.closeButton).setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    // Optionally, you can stop music when the activity is paused
    public void pauseMusic() {
        musicManager.pauseMusic();
    }

    // Optionally, you can resume music when the activity is resumed
    public void resumeMusic() {
        musicManager.resumeMusic();
    }
}
