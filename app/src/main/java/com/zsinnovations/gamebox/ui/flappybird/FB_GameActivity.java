package com.zsinnovations.gamebox.ui.flappybird;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.zsinnovations.gamebox.R;

public class FB_GameActivity extends AppCompatActivity
{
    private ImageView bird, enemy1, enemy2, enemy3, coin1, coin2, right1, right2, right3;
    private TextView textViewScore, textViewStartInfo;
    private ConstraintLayout constraintLayout;

    private boolean touchControl = false;
    private boolean beginControl = false;

    private Runnable runnable;
    private Handler handler, handler2;

    // Positions
    int birdX, enemy1x, enemy2x, enemy3x, coin1x, coin2x;
    int birdY, enemy1y, enemy2y, enemy3y, coin1y, coin2y;
    // Dimensions
    int screenWidth, screenHeight;
    // Remaining Lives (Rights)
    int right = 3;
    // Points
    int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fb_game);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bird = findViewById(R.id.ImageViewBird);
        enemy1 = findViewById(R.id.ImageViewEnemy1);
        enemy2 = findViewById(R.id.ImageViewEnemy2);
        enemy3 = findViewById(R.id.ImageViewEnemy3);
        coin1 = findViewById(R.id.ImageViewCoin);
        coin2 = findViewById(R.id.ImageViewCoin2);
        right1 = findViewById(R.id.ImageViewRight1);
        right2 = findViewById(R.id.ImageViewRight2);
        right3 = findViewById(R.id.ImageViewRight3);
        textViewScore = findViewById(R.id.TextViewScore);
        textViewStartInfo = findViewById(R.id.TextViewStartInfo);
        constraintLayout = findViewById(R.id.main);

        constraintLayout.setOnTouchListener((v, event) -> {
            textViewStartInfo.setVisibility(View.INVISIBLE);
            if (!beginControl)
            {
                beginControl = true;

                screenWidth = (int) constraintLayout.getWidth();
                screenHeight = (int) constraintLayout.getHeight();

                birdX = (int) bird.getX();
                birdY = (int) bird.getY();

                handler = new Handler();
                runnable = new Runnable()
                {
                    @Override
                    public void run()
                    {
                        moveToBird();
                        enemyControl();
                        collisionControl(); // Call collision control here
                        handler.postDelayed(this, 20); // Keep the game loop running
                    }
                };
                handler.post(runnable);
            }
            else
            {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    touchControl = true;
                }

                if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    touchControl = false;
                }
            }

            return true;
        });
    }

    public void moveToBird()
    {
        if (touchControl)
        {
            birdY -= (screenHeight / 50);
        }
        else
        {
            birdY += (screenHeight / 50);
        }

        // Boundary checks
        if (birdY <= 0)
        {
            birdY = 0;
        }
        if (birdY >= (screenHeight - bird.getHeight()))
        {
            birdY = (screenHeight - bird.getHeight());
        }
        bird.setY(birdY);
    }

    public void enemyControl()
    {
        enemy1.setVisibility(View.VISIBLE);
        enemy2.setVisibility(View.VISIBLE);
        enemy3.setVisibility(View.VISIBLE);
        coin1.setVisibility(View.VISIBLE);
        coin2.setVisibility(View.VISIBLE);

        // Enemy 1 Movement
        enemy1x -= (screenWidth / 150);

        if (score >= 100)
        {
            enemy1x -= (screenWidth / 130);
        }

        if (enemy1x < 0)
        {
            enemy1x = screenWidth + 200;
            enemy1y = (int) Math.floor(Math.random() * screenHeight);

            if (enemy1y <= 0)
                enemy1y = 0;

            if (enemy1y >= (screenHeight - enemy1.getHeight()))
                enemy1y = (screenHeight - enemy1.getHeight());
        }
        enemy1.setX(enemy1x);
        enemy1.setY(enemy1y);

        // Enemy 2 Movement
        enemy2x -= (screenWidth / 140);

        if (score >= 100)
        {
            enemy1x -= (screenWidth / 120);
        }

        if (enemy2x < 0)
        {
            enemy2x = screenWidth + 200;
            enemy2y = (int) Math.floor(Math.random() * screenHeight);

            if (enemy2y <= 0)
                enemy2y = 0;

            if (enemy2y >= (screenHeight - enemy2.getHeight()))
                enemy2y = (screenHeight - enemy2.getHeight());
        }
        enemy2.setX(enemy2x);
        enemy2.setY(enemy2y);

        // Enemy 3 Movement
        enemy3x -= (screenWidth / 130);

        if (score >= 100)
        {
            enemy1x -= (screenWidth / 110);
        }

        if (enemy3x < 0)
        {
            enemy3x = screenWidth + 200;
            enemy3y = (int) Math.floor(Math.random() * screenHeight);

            if (enemy3y <= 0)
                enemy3y = 0;

            if (enemy3y >= (screenHeight - enemy3.getHeight()))
                enemy3y = (screenHeight - enemy3.getHeight());
        }
        enemy3.setX(enemy3x);
        enemy3.setY(enemy3y);

        // Coin 1 Movement
        coin1x -= (screenWidth / 120);
        if (coin1x < 0)
        {
            coin1x = screenWidth + 200;
            coin1y = (int) Math.floor(Math.random() * screenHeight);

            if (coin1y <= 0)
                coin1y = 0;

            if (coin1y >= (screenHeight - coin1.getHeight()))
                coin1y = (screenHeight - coin1.getHeight());
        }
        coin1.setX(coin1x);
        coin1.setY(coin1y);

        // Coin 2 Movement
        coin2x -= (screenWidth / 110);
        if (coin2x < 0)
        {
            coin2x = screenWidth + 200;
            coin2y = (int) Math.floor(Math.random() * screenHeight);

            if (coin2y <= 0)
                coin2y = 0;

            if (coin2y >= (screenHeight - coin2.getHeight()))
                coin2y = (screenHeight - coin2.getHeight());
        }
        coin2.setX(coin2x);
        coin2.setY(coin2y);
    }

    public void collisionControl()
    {
        // Enemy 1 Collision
        int centerEnemy1x = enemy1x + enemy1.getWidth() / 2;
        int centerEnemy1y = enemy1y + enemy1.getHeight() / 2;

        if (centerEnemy1x >= birdX &&
                centerEnemy1x <= (birdX + bird.getWidth()) &&
                centerEnemy1y >= birdY &&
                centerEnemy1y <= (birdY + bird.getHeight()))
        {
            enemy1x = screenWidth + 200;
            right--;
        }

        // Enemy 2 Collision
        int centerEnemy2x = enemy2x + enemy2.getWidth() / 2;
        int centerEnemy2y = enemy2y + enemy2.getHeight() / 2;

        if (centerEnemy2x >= birdX &&
                centerEnemy2x <= (birdX + bird.getWidth()) &&
                centerEnemy2y >= birdY &&
                centerEnemy2y <= (birdY + bird.getHeight()))
        {
            enemy2x = screenWidth + 200;
            right--;
        }

        // Enemy 3 Collision
        int centerEnemy3x = enemy3x + enemy3.getWidth() / 2;
        int centerEnemy3y = enemy3y + enemy3.getHeight() / 2;

        if (centerEnemy3x >= birdX &&
                centerEnemy3x <= (birdX + bird.getWidth()) &&
                centerEnemy3y >= birdY &&
                centerEnemy3y <= (birdY + bird.getHeight()))
        {
            enemy3x = screenWidth + 200;
            right--;
        }

        // Coin 1 Collision
        int centerCoin1x = coin1x + coin1.getWidth() / 2;
        int centerCoin1y = coin1y + coin1.getHeight() / 2;
        if (centerCoin1x >= birdX &&
                centerCoin1x <= (birdX + bird.getWidth()) &&
                centerCoin1y >= birdY &&
                centerCoin1y <= (birdY + bird.getHeight()))
        {
            coin1x = screenWidth + 200;
            score += 10;
            textViewScore.setText(String.valueOf(score));
        }

        // Coin 2 Collision
        int centerCoin2x = coin2x + coin2.getWidth() / 2;
        int centerCoin2y = coin2y + coin2.getHeight() / 2;

        if (centerCoin2x >= birdX &&
                centerCoin2x <= (birdX + bird.getWidth()) &&
                centerCoin2y >= birdY &&
                centerCoin2y <= (birdY + bird.getHeight()))
        {
            coin2x = screenWidth + 200;
            score += 10;
            textViewScore.setText(String.valueOf(score));
        }

        // Rights and endgame handling
        if (right > 0 && score < 200)
        {
            if (right == 2) right1.setImageResource(R.drawable.health_empty);
            if (right == 1) right2.setImageResource(R.drawable.health_empty);
        }
        else if (score >= 200)
        {
            endGame(true);
        }
        else if (right == 0)
        {
            endGame(false);
        }
    }
}