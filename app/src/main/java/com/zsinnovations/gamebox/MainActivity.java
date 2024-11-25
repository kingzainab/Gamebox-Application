package com.zsinnovations.gamebox;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.slider.Slider;
import com.zsinnovations.gamebox.ui.mainscreen.FavouriteFragment;
import com.zsinnovations.gamebox.ui.mainscreen.GameFragment;
import com.zsinnovations.gamebox.utils.AvatarManager;
import com.zsinnovations.gamebox.utils.MusicManager;

public class MainActivity extends AppCompatActivity {

    private MusicManager musicManager;
    private AvatarManager avatarManager;
    private static final String PREF_MUSIC_VOLUME = "musicVolume";
    private static final String PREF_MUSIC_ENABLED = "musicEnabled";

    private final int[] predefinedImages = {
            R.drawable.a,
            R.drawable.b,
            R.drawable.c,
            R.drawable.d,
            R.drawable.e
    };
    private int currentAvatarResource = R.drawable.a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize MusicManager
        musicManager = new MusicManager();
        musicManager.startMusic(this);

        // Initialize AvatarManager
        ImageView avatarImageView = findViewById(R.id.avatarIcon);
        avatarManager = new AvatarManager(this, avatarImageView, predefinedImages, currentAvatarResource);
        avatarManager.initialize();

        // Add default fragment (Main Screen)
        if (savedInstanceState == null) {
            loadFragment(new GameFragment());
        }

        // Set up footer navigation
        setupFooterNavigation();

        // Settings icon listener
        ImageView settingsIcon = findViewById(R.id.settingsIcon);
        settingsIcon.setOnClickListener(v -> showSettingsDialog());
    }

    private void setupFooterNavigation() {
        ImageView mainButton = findViewById(R.id.imageView2);
        ImageView mainButtonFilled = findViewById(R.id.imageView4);
        ImageView favoritesButton = findViewById(R.id.imageView3);
        ImageView favoritesButtonFilled = findViewById(R.id.imageView5);

        mainButtonFilled.setOnClickListener(v -> {
            mainButton.setVisibility(View.INVISIBLE);
            favoritesButtonFilled.setVisibility(View.INVISIBLE);
            loadFragment(new GameFragment());
        });

        mainButton.setOnClickListener(v -> {
            mainButton.setVisibility(View.INVISIBLE);
            mainButtonFilled.setVisibility(View.VISIBLE);
            favoritesButtonFilled.setVisibility(View.INVISIBLE);
            loadFragment(new GameFragment());
        });

        favoritesButton.setOnClickListener(v -> {
            favoritesButtonFilled.setVisibility(View.VISIBLE);
            mainButtonFilled.setVisibility(View.INVISIBLE);
            loadFragment(new FavouriteFragment());
            mainButton.setVisibility(View.VISIBLE);
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        musicManager.pauseMusic();
    }

    @Override
    protected void onResume() {
        super.onResume();
        musicManager.resumeMusic();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        musicManager.stopMusic();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_settings, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.setView(dialogView);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Initialize components
        Switch toggleMusicSwitch = dialogView.findViewById(R.id.musicToggleButton);
        Slider volumeSlider = dialogView.findViewById(R.id.volumeSlider);
        TextView volumeLabel = dialogView.findViewById(R.id.volumeLabel);

        SharedPreferences prefs = getSharedPreferences("AppPreferences", MODE_PRIVATE);

        // Load saved music state
        boolean isMusicEnabled = prefs.getBoolean(PREF_MUSIC_ENABLED, true);
        toggleMusicSwitch.setChecked(isMusicEnabled);

        // Customize switch color based on state
        int enabledColor = Color.parseColor("#15BDA3"); // Green for enabled
        int disabledColor = Color.parseColor("#9E9E9E"); // Gray for disabled


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
                musicManager.startMusic(this);
            } else {
                musicManager.pauseMusic();
            }
        });

        // Set up Slider listener
        volumeSlider.addOnChangeListener((slider, value, fromUser) -> {
            int volume = (int) value;
            volumeLabel.setText("Music Volume: " + volume + "%");
            musicManager.setVolume(volume / 100f);

            if (fromUser) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt(PREF_MUSIC_VOLUME, volume);  // Store as integer instead of float
                editor.apply();
            }
        });


        dialogView.findViewById(R.id.closeButton).setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
}