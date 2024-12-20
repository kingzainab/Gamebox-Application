package com.zsinnovations.gamebox.ui.snakegame;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.ViewModel;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Random;

public class SnakeGameViewModel extends ViewModel {

    private static final String PREFS_NAME = "SnakeGamePrefs";
    private static final String HIGHEST_SCORE_KEY = "HighestScore";

    public enum Direction {
        UP, DOWN, LEFT, RIGHT;

        public boolean isOpposite(Direction other) {
            return (this == UP && other == DOWN) ||
                    (this == DOWN && other == UP) ||
                    (this == LEFT && other == RIGHT) ||
                    (this == RIGHT && other == LEFT);
        }
    }

    private static class GameState {
        Deque<int[]> snake;
        int[] apple;
        int score;
        boolean isGameOver;

        GameState() {
            snake = new LinkedList<>();
            snake.add(new int[]{20, 20});
            snake.add(new int[]{19, 20});
            snake.add(new int[]{18, 20});
            apple = new int[]{10, 10};
            score = 0;
            isGameOver = false;
        }
    }

    private final GameState gameState = new GameState();
    private Direction currentDirection = Direction.RIGHT;
    private String temporaryMessage = null;

    public Deque<int[]> getSnake() {
        return gameState.snake;
    }

    public int[] getApple() {
        return gameState.apple;
    }

    public int getScore() {
        return gameState.score;
    }

    public boolean isGameOver() {
        return gameState.isGameOver;
    }

    public void changeDirection(Direction newDirection) {
        if (!currentDirection.isOpposite(newDirection)) {
            currentDirection = newDirection;
        }
    }

    public void startGame(int width, int height) {
        gameState.snake.clear();
        gameState.snake.add(new int[]{width / 2, height / 2});
        gameState.snake.add(new int[]{(width / 2) - 1, height / 2});
        gameState.snake.add(new int[]{(width / 2) - 2, height / 2});
        spawnApple(width, height);
    }

    public boolean updateGame(int width, int height) {
        if (gameState.isGameOver) return false;

        int[] head = gameState.snake.getFirst();
        int[] newHead = new int[]{head[0], head[1]};

        switch (currentDirection) {
            case UP:
                newHead[1]--;
                break;
            case DOWN:
                newHead[1]++;
                break;
            case LEFT:
                newHead[0]--;
                break;
            case RIGHT:
                newHead[0]++;
                break;
        }

        if (isCollision(newHead, width, height)) {
            gameState.isGameOver = true;
            return false;
        }

        gameState.snake.addFirst(newHead);

        if (didEatApple()) {
            gameState.score += 10;
            spawnApple(width, height);
            return true; // Apple was eaten
        } else {
            gameState.snake.removeLast();
            return false; // No apple eaten
        }
    }

    private boolean isCollision(int[] head, int width, int height) {
        if (head[0] < 0 || head[1] < 0 || head[0] >= width || head[1] >= height) {
            return true;
        }

        for (int[] snakePart : gameState.snake) {
            if (head[0] == snakePart[0] && head[1] == snakePart[1]) {
                return true;
            }
        }
        return false;
    }

    private void spawnApple(int width, int height) {
        Random random = new Random();
        boolean validApplePosition;
        int appleX, appleY;
        do {
            appleX = random.nextInt(width);
            appleY = random.nextInt(height);
            validApplePosition = true;

            for (int[] snakePart : gameState.snake) {
                if (snakePart[0] == appleX && snakePart[1] == appleY) {
                    validApplePosition = false;
                    break;
                }
            }
        } while (!validApplePosition);

        gameState.apple[0] = appleX;
        gameState.apple[1] = appleY;
    }

    public void saveHighestScore(Context context, int score) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int currentHighestScore = prefs.getInt(HIGHEST_SCORE_KEY, 0);

        if (score > currentHighestScore) {
            prefs.edit().putInt(HIGHEST_SCORE_KEY, score).apply();
        }
    }

    public int getHighestScore(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(HIGHEST_SCORE_KEY, 0);
    }

    public boolean didEatApple() {
        if (gameState.snake.isEmpty()) return false;

        int[] head = gameState.snake.getFirst();
        return head[0] == gameState.apple[0] && head[1] == gameState.apple[1];
    }
}
