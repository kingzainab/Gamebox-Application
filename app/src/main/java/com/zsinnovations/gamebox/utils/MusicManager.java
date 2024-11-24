package com.zsinnovations.gamebox.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import com.zsinnovations.gamebox.R;

public class MusicManager {
    private MediaPlayer mediaPlayer;

    private Context appContext;

    public void startMusic(Context context) {
        this.appContext = context.getApplicationContext(); // Save the context
        SharedPreferences prefs = appContext.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        boolean isMusicEnabled = prefs.getBoolean("musicEnabled", true);

        if (isMusicEnabled) {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(appContext, R.raw.game_music);
                mediaPlayer.setLooping(true);
            }

            int savedVolume = prefs.getInt("musicVolume", 50); // Default to 50%
            setVolume(savedVolume / 100f);

            mediaPlayer.start();
        }
    }

    public void resumeMusic() {
        if (appContext != null) {
            SharedPreferences prefs = appContext.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
            boolean isMusicEnabled = prefs.getBoolean("musicEnabled", true);

            if (isMusicEnabled && mediaPlayer != null && !mediaPlayer.isPlaying()) {
                mediaPlayer.start();
            }
        }
    }



    public void pauseMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
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
