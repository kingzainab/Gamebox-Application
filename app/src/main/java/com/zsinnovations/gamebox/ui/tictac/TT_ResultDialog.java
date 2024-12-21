package com.zsinnovations.gamebox.ui.tictac;

import androidx.annotation.NonNull;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.zsinnovations.gamebox.databinding.ActivityTtResultDialogBinding;

public class TT_ResultDialog extends Dialog {

    private final String message;
    private final TT_MainActivity mainActivity;
    private ActivityTtResultDialogBinding binding;

    // Fixed constructor name to match class name
    public TT_ResultDialog(@NonNull Context context, String message, TT_MainActivity mainActivity) {
        super(context);
        this.message = message;
        this.mainActivity = mainActivity;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize view binding
        binding = ActivityTtResultDialogBinding.inflate(getLayoutInflater());
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(binding.getRoot());

        // Use view binding to access views
        binding.messageText.setText(message);
        // Use lambda for click listener
        binding.startAgainButton.setOnClickListener(view -> {
            mainActivity.restartMatch();
            dismiss();
        });
        binding.HomeScreenButton.setOnClickListener(view -> {
            // Create an intent to start TT_AddPlayer Activity
            Intent intent = new Intent(getContext(), TT_AddPlayer.class);
            getContext().startActivity(intent);
            dismiss();
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Clean up binding
        binding = null;
    }
}