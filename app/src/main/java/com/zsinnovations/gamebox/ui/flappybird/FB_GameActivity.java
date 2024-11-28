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
}