package com.zsinnovations.gamebox;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.zsinnovations.gamebox.adapters.ImageAdapter;
import com.zsinnovations.gamebox.fragments.FavoritesFragment;
import com.zsinnovations.gamebox.fragments.GamesFragment;

public class MainActivity extends AppCompatActivity {
    private ImageView avatarImageView;
    private int[] predefinedImages = {
            R.drawable.a,
            R.drawable.b,
            R.drawable.c,
            R.drawable.d,
            R.drawable.e
    };
    private int currentAvatarResource = R.drawable.a; // Store the current avatar resource

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        avatarImageView = findViewById(R.id.avatarIcon);
        avatarImageView.setImageResource(currentAvatarResource); // Set default avatar

        avatarImageView.setOnClickListener(v -> showPredefinedImageDialog());

        // ... (rest of onCreate remains the same)
    }

    private void showPredefinedImageDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_predefined_images, null);

        GridView gridView = dialogView.findViewById(R.id.gridView);
        // Pass the current avatar resource to the adapter
        gridView.setAdapter(new ImageAdapter(this, predefinedImages, currentAvatarResource));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setTitle("Choose an Avatar")
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();

        gridView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            currentAvatarResource = predefinedImages[position]; // Update the current avatar resource
            avatarImageView.setImageResource(currentAvatarResource);
            Toast.makeText(this, "Avatar updated!", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
    }
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;

        if (item.getItemId() == R.id.nav_games) {
            selectedFragment = new GamesFragment();
        } else if (item.getItemId() == R.id.nav_favorites) {
            selectedFragment = new FavoritesFragment();
        }

        if (selectedFragment != null) {
            loadFragment(selectedFragment);
            return true;
        }
        return false;
    }
}
