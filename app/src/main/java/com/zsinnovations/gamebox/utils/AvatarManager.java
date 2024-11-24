package com.zsinnovations.gamebox.utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.drawable.ColorDrawable;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.zsinnovations.gamebox.R;
import com.zsinnovations.gamebox.adapters.AvatarAdapter;

public class AvatarManager {
    private static final String PREFS_NAME = "AvatarPreferences";
    private static final String AVATAR_KEY = "selectedAvatar";

    private final Activity activity;
    private final ImageView avatarImageView;
    private final int[] avatarImages;
    private int selectedAvatarResource;

    public AvatarManager(Activity activity, ImageView avatarImageView, int[] avatarImages, int defaultAvatar) {
        this.activity = activity;
        this.avatarImageView = avatarImageView;
        this.avatarImages = avatarImages;
        this.selectedAvatarResource = loadAvatarResource(defaultAvatar);
    }

    public void initialize() {
        avatarImageView.setImageResource(selectedAvatarResource);
        avatarImageView.setOnClickListener(v -> showAvatarSelector());
    }

    private void showAvatarSelector() {
        // Use AlertDialog for a centered dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        View dialogView = activity.getLayoutInflater().inflate(R.layout.dialog_avatar_selector, null);

        TextView titleText = dialogView.findViewById(R.id.titleText);
        GridView gridView = dialogView.findViewById(R.id.avatarGrid);
        CardView closeButton = dialogView.findViewById(R.id.closeButton);

        titleText.setText("Avatar");

        AvatarAdapter adapter = new AvatarAdapter(activity, avatarImages, selectedAvatarResource);
        gridView.setAdapter(adapter);
        AlertDialog dialog = builder.setView(dialogView).create();
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            selectedAvatarResource = avatarImages[position];
            avatarImageView.setImageResource(selectedAvatarResource);
            saveAvatarResource(selectedAvatarResource);
            dialog.dismiss();
        });

        closeButton.setOnClickListener(v -> dialog.dismiss());

        // Create a custom AlertDialog


        // Set the dialog to have a transparent background if needed
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        // Show the dialog
        dialog.show();
        dialog.getWindow().setGravity(Gravity.CENTER);
    }


    private void saveAvatarResource(int resource) {
        SharedPreferences prefs = activity.getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
        prefs.edit().putInt(AVATAR_KEY, resource).apply();
    }

    private int loadAvatarResource(int defaultResource) {
        SharedPreferences prefs = activity.getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
        return prefs.getInt(AVATAR_KEY, defaultResource);
    }
}
