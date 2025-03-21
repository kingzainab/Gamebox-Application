package com.zsinnovations.gamebox.ui.snakegame;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RotateDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.zsinnovations.gamebox.R;

public class SG_SplashScreen extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_LENGTH = 3000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sg_splash_screen);
        ProgressBar spinner = findViewById(R.id.loading_spinner2);

        RotateDrawable rotateDrawable = (RotateDrawable) spinner.getIndeterminateDrawable();

        GradientDrawable gradientDrawable = (GradientDrawable) rotateDrawable.getDrawable();

        int newEndColor = ContextCompat.getColor(this, R.color.white);
        gradientDrawable.setColors(new int[]{gradientDrawable.getColors()[0], newEndColor});

        new Handler().postDelayed(() -> {
            Intent mainIntent = new Intent(SG_SplashScreen.this, SG_MainActivity.class);
            startActivity(mainIntent);
            finish();
        }, SPLASH_DISPLAY_LENGTH);
    }
}