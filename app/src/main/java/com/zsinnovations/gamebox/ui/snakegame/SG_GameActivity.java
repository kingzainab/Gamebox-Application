package com.zsinnovations.gamebox.ui.snakegame;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;

import androidx.activity.ComponentActivity;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.zsinnovations.gamebox.R;


public class SG_GameActivity extends ComponentActivity {
    private SnakeGameViewModel viewModel;
    private GameView gameView;
    private Handler gameHandler;
    private static final int GAME_SPEED = 200; // milliseconds

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sg_game);

        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);

        viewModel = new ViewModelProvider(this).get(SnakeGameViewModel.class);
        gameView = findViewById(R.id.gameView);
        gameView.setViewModel(viewModel);

        viewModel.startGame(size.x / 40, (size.y / 40) - 2);

        setupGameLoop();
        setupControls();
    }

    private void setupGameLoop() {
        gameHandler = new Handler(Looper.getMainLooper());
        gameHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!viewModel.isGameOver()) {
                    Point size = new Point();
                    getWindowManager().getDefaultDisplay().getSize(size);
                    viewModel.updateGame(size.x / 40, (size.y / 40) - 2);
                    gameView.invalidate();
                    gameHandler.postDelayed(this, GAME_SPEED);
                } else {
                    // Game Over Logic
                    viewModel.saveHighestScore(SG_GameActivity.this, viewModel.getScore());
                    Intent resultIntent = new Intent(SG_GameActivity.this, SG_ResultActivity.class);
                    resultIntent.putExtra("SCORE", viewModel.getScore());
                    resultIntent.putExtra("HIGHEST_SCORE", viewModel.getHighestScore(SG_GameActivity.this));
                    startActivity(resultIntent);
                    finish();
                }
            }
        }, GAME_SPEED);
    }

//    private void setupControls() {
//        gameView.setOnTouchListener(new View.OnTouchListener() {
//            private float startX, startY;
//            private static final int MIN_SWIPE_DISTANCE = 100;
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        startX = event.getX();
//                        startY = event.getY();
//                        return true;
//
//                    case MotionEvent.ACTION_UP:
//                        float endX = event.getX();
//                        float endY = event.getY();
//
//                        float deltaX = endX - startX;
//                        float deltaY = endY - startY;
//
//                        // Determine swipe direction based on distance and angle
//                        if (Math.abs(deltaX) > Math.abs(deltaY)) {
//                            // Horizontal swipe
//                            if (Math.abs(deltaX) > MIN_SWIPE_DISTANCE) {
//                                if (deltaX > 0) {
//                                    // Right swipe
//                                    viewModel.changeDirection(SnakeGameViewModel.Direction.RIGHT);
//                                } else {
//                                    // Left swipe
//                                    viewModel.changeDirection(SnakeGameViewModel.Direction.LEFT);
//                                }
//                            }
//                        } else {
//                            // Vertical swipe
//                            if (Math.abs(deltaY) > MIN_SWIPE_DISTANCE) {
//                                if (deltaY > 0) {
//                                    // Down swipe
//                                    viewModel.changeDirection(SnakeGameViewModel.Direction.DOWN);
//                                } else {
//                                    // Up swipe
//                                    viewModel.changeDirection(SnakeGameViewModel.Direction.UP);
//                                }
//                            }
//                        }
//                        return true;
//                }
//                return false;
//            }
//        });
//    }

    private void setupControls() {
        gameView.setOnTouchListener(new View.OnTouchListener() {
            private float startX, startY;
            private static final int MIN_SWIPE_DISTANCE = 50;  // Reduced for more responsive controls
            private static final int MAX_SWIPE_TIME = 500;     // Maximum time for a valid swipe
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
                        float endX = event.getX();
                        float endY = event.getY();
                        long endTime = System.currentTimeMillis();

                        float deltaX = endX - startX;
                        float deltaY = endY - startY;

                        // Check swipe duration to prevent accidental direction changes
                        if (endTime - startTime > MAX_SWIPE_TIME) {
                            return false;
                        }

                        // Use angle-based direction determination for more natural control
                        double angle = Math.toDegrees(Math.atan2(deltaY, deltaX));

                        // Angle-based direction selection with broader ranges
                        if (Math.abs(deltaX) > MIN_SWIPE_DISTANCE || Math.abs(deltaY) > MIN_SWIPE_DISTANCE) {
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
                        return true;
                }
                return false;
            }
        });
    }
}
