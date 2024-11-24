package com.zsinnovations.gamebox.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import com.zsinnovations.gamebox.R;

public class MusicManager {
    private MediaPlayer mediaPlayer;

    public void startMusic(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        boolean isMusicEnabled = prefs.getBoolean("musicEnabled", true);

        if (isMusicEnabled) {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(context, R.raw.game_music);
                mediaPlayer.setLooping(true);
            }

            // Set the saved volume level
            int savedVolume = prefs.getInt("musicVolume", 50); // Default to 50%
            setVolume(savedVolume / 100f);

            mediaPlayer.start();
        }
    }

    public void pauseMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public void resumeMusic(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        boolean isMusicEnabled = prefs.getBoolean("musicEnabled", true); // Default to true

        if (isMusicEnabled && mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    public void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void setVolume(float volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume, volume); // Apply volume to both left and right channels
        }
    }
}
