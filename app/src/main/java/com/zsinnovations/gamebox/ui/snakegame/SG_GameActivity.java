package com.zsinnovations.gamebox.ui.snakegame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowMetrics;

import androidx.activity.ComponentActivity;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import com.zsinnovations.gamebox.R;

public class SG_GameActivity extends ComponentActivity {
    private static final String TAG = "SG_GameActivity";
    private static final String PREFS_NAME = "SnakeGamePrefs";
    private static final String KEY_SOUND_ENABLED = "SoundEnabled";
    private static final int GAME_SPEED = 200;
    private static final int MIN_SWIPE_DISTANCE = 50;
    private static final int MAX_SWIPE_TIME = 500;
    private SnakeGameViewModel viewModel;
    private GameView gameView;
    private Handler gameHandler;
    private MediaPlayer gameOverSound;
    private SharedPreferences preferences;
    private boolean isSoundEnabled = true;
    private boolean isGamePaused = false;

    private void initializeSoundSystem() {
        preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        isSoundEnabled = preferences.getBoolean(KEY_SOUND_ENABLED, true);

        try {
            // Initialize game over sound only
            gameOverSound = MediaPlayer.create(this, R.raw.level_end);
            if (gameOverSound == null) {
                throw new IllegalStateException("Failed to create game over sound");
            }
            configureMediaPlayer(gameOverSound, "Game Over");

        } catch (Exception e) {
            isSoundEnabled = false;

        }
    }

    private void configureMediaPlayer(MediaPlayer player, String soundName) {
        if (player != null) {
            player.setVolume(1.0f, 1.0f);
            player.setOnPreparedListener(mp -> Log.d(TAG, soundName + " MediaPlayer prepared"));
            player.setOnErrorListener((mp, what, extra) -> {
                handleSoundError();
                return true;
            });
            player.setOnCompletionListener(mp -> {
                mp.seekTo(0);
            });
        }
    }

    private void handleSoundError() {
        isSoundEnabled = false;
        preferences.edit().putBoolean(KEY_SOUND_ENABLED, false).apply();

        // Attempt to reinitialize sound system
        initializeSoundSystem();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sg_game);

        preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        initializeSoundSystem();
        initializeGameComponents();
        setupGameControls();
        setupBackPressHandling();
    }

    private void initializeGameComponents() {
        viewModel = new ViewModelProvider(this).get(SnakeGameViewModel.class);
        gameView = findViewById(R.id.gameView);
        gameView.setViewModel(viewModel);

        int[] screenMetrics = getScreenMetrics();
        int screenWidth = screenMetrics[0];
        int screenHeight = screenMetrics[1];

        // Initialize game grid
        viewModel.startGame(screenWidth / 40, (screenHeight / 40) - 2);

        setupGameLoop();
    }

    private int[] getScreenMetrics() {
        int screenWidth;
        int screenHeight;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowMetrics metrics = getWindowManager().getCurrentWindowMetrics();
            Rect bounds = metrics.getBounds();
            screenWidth = bounds.width();
            screenHeight = bounds.height();
        } else {
            Point screenSize = new Point();
            getWindowManager().getDefaultDisplay().getSize(screenSize);
            screenWidth = screenSize.x;
            screenHeight = screenSize.y;
        }

        return new int[]{screenWidth, screenHeight};
    }

    private void setupGameLoop() {
        gameHandler = new Handler(Looper.getMainLooper());
        gameHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isGamePaused) {
                    gameHandler.postDelayed(this, GAME_SPEED);
                    return;
                }

                if (!viewModel.isGameOver()) {
                    handleActiveGameState();
                    gameHandler.postDelayed(this, GAME_SPEED);
                } else {
                    handleGameOverState();
                }
            }
        }, GAME_SPEED);
    }

    private void handleActiveGameState() {
        int[] screenMetrics = getScreenMetrics();
        int screenWidth = screenMetrics[0];
        int screenHeight = screenMetrics[1];

        viewModel.updateGame(screenWidth / 40, (screenHeight / 40) - 2);
        gameView.invalidate();
    }



    private void handleGameOverState() {

        saveGameState();
        showGameResults();

    }

    private void saveGameState() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("LastScore", viewModel.getScore());
        editor.apply();
    }

    private void showGameResults() {
        int currentScore = viewModel.getScore();
        viewModel.saveHighestScore(this, currentScore);

        Intent resultIntent = new Intent(this, SG_ResultActivity.class);
        resultIntent.putExtra("SCORE", currentScore);
        resultIntent.putExtra("HIGHEST_SCORE", viewModel.getHighestScore(this));
        startActivity(resultIntent);
        finish();
    }

    private void setupGameControls() {
        gameView.setOnTouchListener(new View.OnTouchListener() {
            private float startX, startY;
            private long startTime;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        startTime = System.currentTimeMillis();
                        return true;

                    case MotionEvent.ACTION_UP:
                        return handleSwipeInput(event, startX, startY, startTime);
                }
                return false;
            }
        });
    }

    private boolean handleSwipeInput(MotionEvent event, float startX, float startY, long startTime) {
        float endX = event.getX();
        float endY = event.getY();
        long endTime = System.currentTimeMillis();

        float deltaX = endX - startX;
        float deltaY = endY - startY;

        if (endTime - startTime > MAX_SWIPE_TIME) {
            return false;
        }

        double angle = Math.toDegrees(Math.atan2(deltaY, deltaX));

        if (Math.abs(deltaX) > MIN_SWIPE_DISTANCE || Math.abs(deltaY) > MIN_SWIPE_DISTANCE) {
            determineAndChangeDirection(angle);
        }
        return true;
    }

    private void determineAndChangeDirection(double angle) {
        if (angle >= -135 && angle <= -45) {
            viewModel.changeDirection(SnakeGameViewModel.Direction.UP);
        } else if (angle > -45 && angle < 45) {
            viewModel.changeDirection(SnakeGameViewModel.Direction.RIGHT);
        } else if (angle >= 45 && angle <= 135) {
            viewModel.changeDirection(SnakeGameViewModel.Direction.DOWN);
        } else {
            viewModel.changeDirection(SnakeGameViewModel.Direction.LEFT);
        }
    }

    private void setupBackPressHandling() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showExitConfirmationDialog();
            }
        });
    }

    private void showExitConfirmationDialog() {
        isGamePaused = true;

        new AlertDialog.Builder(this)
                .setTitle("Snake Game")
                .setMessage("Are you sure you want to quit the game?")
                .setCancelable(false)
                .setNegativeButton("Quit", (dialog, which) -> {
                    Intent intent = new Intent(this, SG_MainActivity.class);
                    startActivity(intent);
                    finish();
                })
                .setPositiveButton("Resume", (dialog, which) -> isGamePaused = false)
                .show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isGamePaused = true;
        saveGameState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isGamePaused = false;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gameHandler != null) {
            gameHandler.removeCallbacksAndMessages(null);
        }

    }




}
