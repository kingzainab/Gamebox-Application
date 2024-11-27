package com.zsinnovations.gamebox;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.zsinnovations.gamebox.ui.mainscreen.FavouriteFragment;
import com.zsinnovations.gamebox.ui.mainscreen.GameFragment;
import com.zsinnovations.gamebox.utils.AvatarManager;
import com.zsinnovations.gamebox.utils.MusicManager;
import com.zsinnovations.gamebox.utils.SettingsManager;

public class MainActivity extends AppCompatActivity {

    private SettingsManager settingsManager;
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

        // Initialize SettingsManager
        settingsManager = new SettingsManager(this);

        // Start or resume music only if not already playing
        if (!MusicManager.isPlaying()) {
            settingsManager.startMusic();
        }

        // Load default fragment
        if (savedInstanceState == null) {
            loadFragment(new GameFragment());
        }


        ImageView avatarImageView = findViewById(R.id.avatarIcon);
        avatarManager = new AvatarManager(this, avatarImageView, predefinedImages, currentAvatarResource);
        avatarManager.initialize();

        // Settings icon listener
        ImageView settingsIcon = findViewById(R.id.settingsIcon);
        ImageView avatarIcon=findViewById(R.id.avatarIcon);
        settingsIcon.setOnClickListener(v -> settingsManager.showSettingsDialog(this));


        setupFooterNavigation();
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
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
    }
//    private void loadFragmentGame(Fragment fragment) {
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(
//                R.anim.slide_in_left,
//                R.anim.slide_out_right
//
//        );
//        transaction.replace(R.id.fragmentContainer, fragment);
//        transaction.commit();
//    }
//    private void loadFragmentFav(Fragment fragment) {
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(
//                R.anim.slide_in_right,
//                R.anim.slide_out_left
//        );
//        transaction.replace(R.id.fragmentContainer, fragment);
//        transaction.commit();
//    }

    @Override
    protected void onPause() {
        super.onPause();
        settingsManager.pauseMusic();
    }

    @Override
    protected void onResume() {
        super.onResume();
        settingsManager.resumeMusic();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        settingsManager.stopMusic();
    }
}
