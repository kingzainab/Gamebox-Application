package com.zsinnovations.gamebox;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.zsinnovations.gamebox.ui.mainscreen.FavouriteFragment;
import com.zsinnovations.gamebox.ui.mainscreen.game;
import com.zsinnovations.gamebox.utils.AvatarManager;
import com.zsinnovations.gamebox.utils.MusicManager;

public class MainActivity extends AppCompatActivity {

    private MusicManager musicManager;
    private AvatarManager avatarManager;

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
            loadFragment(new game());
        }

        // Set up footer navigation
        setupFooterNavigation();

        // Settings icon listener
        ImageView settingsIcon = findViewById(R.id.settingsIcon);
        settingsIcon.setOnClickListener(v -> showSettingsDialog());
    }

    private void setupFooterNavigation() {
        ImageView mainButton = findViewById(R.id.imageView2); // Main screen button
        ImageView mainButtonFilled = findViewById(R.id.imageView4); // Main screen button
        ImageView favoritesButton = findViewById(R.id.imageView3); // Favorites button
        ImageView favoritesButtonFilled = findViewById(R.id.imageView5); // Favorites button
        // Set listeners for footer buttons
        mainButtonFilled.setOnClickListener(v -> {

            mainButton.setVisibility(View.INVISIBLE);
            favoritesButtonFilled.setVisibility(View.INVISIBLE);
            loadFragment(new game());
        });

        mainButton.setOnClickListener(v -> {

            mainButton.setVisibility(View.INVISIBLE);
            mainButtonFilled.setVisibility(View.VISIBLE);
            favoritesButtonFilled.setVisibility(View.INVISIBLE);
            loadFragment(new game());
        });

        favoritesButton.setOnClickListener(v -> {
            favoritesButtonFilled.setVisibility(View.VISIBLE);
            mainButtonFilled.setVisibility(View.INVISIBLE); // Reset other button background
            loadFragment(new FavouriteFragment());
            mainButton.setVisibility(View.VISIBLE); // Reset other button background
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
        builder.setTitle("Settings");

        // Inflate custom settings dialog layout
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_settings, null);
        builder.setView(dialogView);

        // Initialize components
        Switch musicSwitch = dialogView.findViewById(R.id.musicSwitch);
        SeekBar volumeSeekBar = dialogView.findViewById(R.id.volumeSeekBar);
        TextView volumeLabel = dialogView.findViewById(R.id.volumeLabel);

        SharedPreferences prefs = getSharedPreferences("AppPreferences", MODE_PRIVATE);

        // Load saved music state
        boolean isMusicEnabled = prefs.getBoolean("musicEnabled", true);
        musicSwitch.setChecked(isMusicEnabled);

        // Load saved volume level
        int savedVolume = prefs.getInt("musicVolume", 50); // Default to 50%
        volumeSeekBar.setProgress(savedVolume);
        volumeLabel.setText("Music Volume: " + savedVolume + "%");

        // Set listeners
        musicSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("musicEnabled", isChecked);
            editor.apply();

            if (isChecked) {
                musicManager.startMusic(this);
            } else {
                musicManager.pauseMusic();
            }
        });

        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                volumeLabel.setText("Music Volume: " + progress + "%");
                musicManager.setVolume(progress / 100f); // Update music volume
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("musicVolume", seekBar.getProgress());
                editor.apply();
            }
        });

        builder.setNegativeButton("Close", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }
}
