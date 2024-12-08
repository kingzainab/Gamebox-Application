package com.zsinnovations.gamebox.ui.snakegame;

import android.content.Intent;
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
    private static final int GAME_SPEED = 200; // milliseconds
    private static final int MIN_SWIPE_DISTANCE = 50;
    private static final int MAX_SWIPE_TIME = 500;

    private SnakeGameViewModel viewModel;
    private GameView gameView;
    private Handler gameHandler;
    private MediaPlayer appleEatSound, gameOverSound;
    private boolean isGamePaused = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sg_game);

        initializeSoundSystem();
        initializeGameComponents();
        setupGameControls();
        setupBackPressHandling();
    }

    private void initializeSoundSystem() {
        appleEatSound = MediaPlayer.create(this, R.raw.success);
        gameOverSound = MediaPlayer.create(this, R.raw.level_end);
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

        if (viewModel.didEatApple()) {
            playSound(appleEatSound); // Ensure this plays right after detecting apple consumption
        }

        viewModel.updateGame(screenWidth / 40, (screenHeight / 40) - 2);
        gameView.invalidate();
    }


    private void handleGameOverState() {
        playSound(gameOverSound);

        // Delay transitioning to the results screen
        int soundDuration = gameOverSound != null ? gameOverSound.getDuration() : 0;
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            viewModel.saveHighestScore(this, viewModel.getScore());

            Intent resultIntent = new Intent(this, SG_ResultActivity.class);
            resultIntent.putExtra("SCORE", viewModel.getScore());
            resultIntent.putExtra("HIGHEST_SCORE", viewModel.getHighestScore(this));

            startActivity(resultIntent);
            finish();
        }, soundDuration);
    }


    private void playSound(MediaPlayer sound) {
        if (sound == null) return;
        try {
            if (sound.isPlaying()) {
                sound.pause();
                sound.seekTo(0);
            }
            sound.start();
        } catch (Exception e) {
            Log.e(TAG, "Sound playback error", e);
        }
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
    protected void onDestroy() {
        super.onDestroy();

        releaseSound(appleEatSound);
        releaseSound(gameOverSound);

        if (gameHandler != null) {
            gameHandler.removeCallbacksAndMessages(null);
        }
    }

    private void releaseSound(MediaPlayer sound) {
        if (sound != null) {
            sound.release();
        }
    }
}
