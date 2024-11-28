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
    protected void onCreate(Bundle savedInstanceState) {
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
    }
}