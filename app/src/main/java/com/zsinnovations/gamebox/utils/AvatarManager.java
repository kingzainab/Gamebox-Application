package com.zsinnovations.gamebox.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.zsinnovations.gamebox.R;
import com.zsinnovations.gamebox.adapters.ImageAdapter;

public class AvatarManager {

    private static final String PREFS_NAME = "AppPreferences";
    private static final String AVATAR_KEY = "currentAvatar";

    private final Activity activity;
    private final ImageView avatarImageView;
    private int[] predefinedImages;
    private int currentAvatarResource;

    public AvatarManager(Activity activity, ImageView avatarImageView, int[] predefinedImages, int defaultAvatarResource) {
        this.activity = activity;
        this.avatarImageView = avatarImageView;
        this.predefinedImages = predefinedImages;

        // Load the saved avatar or use the default
        this.currentAvatarResource = loadAvatarResource(defaultAvatarResource);
    }

    public void initialize() {
        // Set the loaded (or default) avatar image
        avatarImageView.setImageResource(currentAvatarResource);

        // Set the click listener to change the avatar
        avatarImageView.setOnClickListener(v -> showPredefinedImageDialog());
    }

    private void showPredefinedImageDialog() {
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_predefined_images, null);

        GridView gridView = dialogView.findViewById(R.id.gridView);
        gridView.setAdapter(new ImageAdapter(activity, predefinedImages, currentAvatarResource));

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(dialogView)
                .setTitle("Choose an Avatar")
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();

        gridView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            // Update the current avatar resource
            currentAvatarResource = predefinedImages[position];
            avatarImageView.setImageResource(currentAvatarResource);

            // Save the selection to SharedPreferences
            saveAvatarResource(currentAvatarResource);

            // Inform the user
            Toast.makeText(activity, "Avatar updated!", Toast.LENGTH_SHORT).show();

            // Dismiss the dialog
            dialog.dismiss();
        });
    }

    private void saveAvatarResource(int avatarResource) {
        // Save the selected avatar to SharedPreferences
        SharedPreferences prefs = activity.getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(AVATAR_KEY, avatarResource);
        editor.apply();
    }

    private int loadAvatarResource(int defaultAvatarResource) {
        // Load the saved avatar from SharedPreferences
        SharedPreferences prefs = activity.getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
        return prefs.getInt(AVATAR_KEY, defaultAvatarResource);
    }
}
