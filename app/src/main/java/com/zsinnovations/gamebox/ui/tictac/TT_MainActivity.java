package com.zsinnovations.gamebox.ui.tictac;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.zsinnovations.gamebox.R;
import com.zsinnovations.gamebox.databinding.ActivityTtMainBinding;

import java.util.ArrayList;
import java.util.List;

public class TT_MainActivity extends AppCompatActivity {

    private ActivityTtMainBinding binding;
    private final List<int[]> combinationList = new ArrayList<>();
    private int[] boxPositions = {0,0,0,0,0,0,0,0,0};
    private int playerTurn = 1;
    private int totalSelectedBoxes = 1;

    private static final String PREFS_NAME = "GameSoundPrefs";
    private static final String KEY_SOUND_ENABLED = "sound_enabled";
    private MediaPlayer moveSound;
    private MediaPlayer winSound;
    private MediaPlayer drawSound;
    private boolean isSoundEnabled;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTtMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializeSounds();
        setupCombinationList();
        setupPlayerNames();
        setupSoundToggle();
        setupClickListeners();
        setupBackPressHandler();
    }

    private void initializeSounds() {
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        isSoundEnabled = prefs.getBoolean(KEY_SOUND_ENABLED, true);

        moveSound = MediaPlayer.create(this, R.raw.coin_collect);
        winSound = MediaPlayer.create(this, R.raw.success);
        drawSound = MediaPlayer.create(this, R.raw.level_end);

        updateSoundToggleIcon();
    }

    private void setupCombinationList() {
        combinationList.add(new int[] {0,1,2});
        combinationList.add(new int[] {3,4,5});
        combinationList.add(new int[] {6,7,8});
        combinationList.add(new int[] {0,3,6});
        combinationList.add(new int[] {1,4,7});
        combinationList.add(new int[] {2,5,8});
        combinationList.add(new int[] {2,4,6});
        combinationList.add(new int[] {0,4,8});
    }

    private void setupPlayerNames() {
        String getPlayerOneName = getIntent().getStringExtra("playerOne");
        String getPlayerTwoName = getIntent().getStringExtra("playerTwo");

        binding.playerOneName.setText(getPlayerOneName);
        binding.playerTwoName.setText(getPlayerTwoName);
    }

    private void setupSoundToggle() {
        binding.levelTwoSoundTictac.setOnClickListener(v -> toggleSound());
        binding.zeroSoundTictac.setOnClickListener(v -> toggleSound());
    }

    private void setupClickListeners() {
        binding.image1.setOnClickListener(view -> tryPerformAction(view, 0));
        binding.image2.setOnClickListener(view -> tryPerformAction(view, 1));
        binding.image3.setOnClickListener(view -> tryPerformAction(view, 2));
        binding.image4.setOnClickListener(view -> tryPerformAction(view, 3));
        binding.image5.setOnClickListener(view -> tryPerformAction(view, 4));
        binding.image6.setOnClickListener(view -> tryPerformAction(view, 5));
        binding.image7.setOnClickListener(view -> tryPerformAction(view, 6));
        binding.image8.setOnClickListener(view -> tryPerformAction(view, 7));
        binding.image9.setOnClickListener(view -> tryPerformAction(view, 8));
    }

    private void setupBackPressHandler() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showExitConfirmationDialog();
            }
        });
    }

    private void tryPerformAction(View view, int position) {
        if (isBoxSelectable(position)) {
            performAction((ImageView) view, position);
        }
    }

    private void toggleSound() {
        isSoundEnabled = !isSoundEnabled;
        prefs.edit().putBoolean(KEY_SOUND_ENABLED, isSoundEnabled).apply();
        updateSoundToggleIcon();
    }

    private void updateSoundToggleIcon() {
        binding.levelTwoSoundTictac.setVisibility(isSoundEnabled ? View.VISIBLE : View.INVISIBLE);
        binding.zeroSoundTictac.setVisibility(isSoundEnabled ? View.INVISIBLE : View.VISIBLE);
    }

    private void playSound(MediaPlayer player) {
        if (isSoundEnabled && player != null && !player.isPlaying()) {
            player.seekTo(0);
            player.start();
        }
    }

    private void showExitConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tic-Tac-Toe");
        builder.setMessage("Are you sure you want to quit the game?");
        builder.setCancelable(false);

        builder.setNegativeButton("Quit", (dialog, which) -> {
            Intent intent = new Intent(TT_MainActivity.this, TT_AddPlayer.class);
            startActivity(intent);
            finish();
        });

        builder.setPositiveButton("Resume", (dialog, which) -> {});
        builder.show();
    }

    private void performAction(ImageView imageView, int selectedBoxPosition) {
        boxPositions[selectedBoxPosition] = playerTurn;

        if (playerTurn == 1) {
            imageView.setImageResource(R.drawable.ximage);
            playSound(moveSound);

            if (checkResults()) {
                playSound(winSound);
                showResultDialog(binding.playerOneName.getText().toString() + " is a Winner!");
            } else if(totalSelectedBoxes == 9) {
                playSound(drawSound);
                showResultDialog("Match Draw");
            } else {
                changePlayerTurn(2);
                totalSelectedBoxes++;
            }
        } else {
            imageView.setImageResource(R.drawable.oimage);
            playSound(moveSound);

            if (checkResults()) {
                playSound(winSound);
                showResultDialog(binding.playerTwoName.getText().toString() + " is a Winner!");
            } else if(totalSelectedBoxes == 9) {
                playSound(drawSound);
                showResultDialog("Match Draw");
            } else {
                changePlayerTurn(1);
                totalSelectedBoxes++;
            }
        }
    }

    private void showResultDialog(String message) {
        TT_ResultDialog resultDialog = new TT_ResultDialog(TT_MainActivity.this,
                message, TT_MainActivity.this);
        resultDialog.setCancelable(false);
        resultDialog.show();
    }

    private void changePlayerTurn(int currentPlayerTurn) {
        playerTurn = currentPlayerTurn;
        if (playerTurn == 1) {
            binding.playerOneLayout.setBackgroundResource(R.drawable.black_border);
            binding.playerTwoLayout.setBackgroundResource(R.drawable.white_box);
        } else {
            binding.playerTwoLayout.setBackgroundResource(R.drawable.black_border);
            binding.playerOneLayout.setBackgroundResource(R.drawable.white_box);
        }
    }

    private boolean checkResults() {
        for (int[] combination : combinationList) {
            if (boxPositions[combination[0]] == playerTurn &&
                    boxPositions[combination[1]] == playerTurn &&
                    boxPositions[combination[2]] == playerTurn) {
                return true;
            }
        }
        return false;
    }

    private boolean isBoxSelectable(int boxPosition) {
        return boxPositions[boxPosition] == 0;
    }

    public void restartMatch() {
        boxPositions = new int[] {0,0,0,0,0,0,0,0,0};
        playerTurn = 1;
        totalSelectedBoxes = 1;

        binding.image1.setImageResource(R.drawable.white_box);
        binding.image2.setImageResource(R.drawable.white_box);
        binding.image3.setImageResource(R.drawable.white_box);
        binding.image4.setImageResource(R.drawable.white_box);
        binding.image5.setImageResource(R.drawable.white_box);
        binding.image6.setImageResource(R.drawable.white_box);
        binding.image7.setImageResource(R.drawable.white_box);
        binding.image8.setImageResource(R.drawable.white_box);
        binding.image9.setImageResource(R.drawable.white_box);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (moveSound != null) {
            moveSound.release();
            moveSound = null;
        }
        if (winSound != null) {
            winSound.release();
            winSound = null;
        }
        if (drawSound != null) {
            drawSound.release();
            drawSound = null;
        }
        binding = null;
    }
}