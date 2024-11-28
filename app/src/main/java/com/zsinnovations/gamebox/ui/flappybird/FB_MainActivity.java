package com.zsinnovations.gamebox.ui.flappybird;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.zsinnovations.gamebox.R;

public class FB_MainActivity extends AppCompatActivity
{
    private ImageView bird, enemy1, enemy2, enemy3, coin, volume;
    private Button buttonStart;
    private Animation animation;
    private MediaPlayer mediaPlayer;
    boolean status = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);  // Enable Edge-to-edge functionality
        setContentView(R.layout.activity_main);  // Set the layout for this activity

        // Set up the padding for system bars (like status bar, navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views (make sure the IDs are correct in your XML layout file)
        bird = findViewById(R.id.bird);
        enemy1 = findViewById(R.id.enemy1);
        enemy2 = findViewById(R.id.enemy2);
        enemy3 = findViewById(R.id.enemy3);
        coin = findViewById(R.id.coin);
        volume = findViewById(R.id.volume);
        buttonStart = findViewById(R.id.buttonStart);

        animation = AnimationUtils.loadAnimation(FB_MainActivity.this, R.anim.scale_animation);
        bird.setAnimation(animation);
        enemy1.setAnimation(animation);
        enemy2.setAnimation(animation);
        enemy3.setAnimation(animation);
        coin.setAnimation(animation);

    }

}
