package com.zsinnovations.gamebox.ui.snakegame;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RotateDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.zsinnovations.gamebox.R;
import com.zsinnovations.gamebox.ui.balloonburst.BG_MainActivity;
import com.zsinnovations.gamebox.ui.balloonburst.BG_SplashScreen;

public class SG_Splash_Screen extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_LENGTH = 3000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sg_splash_screen);
        ProgressBar spinner = findViewById(R.id.loading_spinner2);

        // Get the RotateDrawable from the ProgressBar
        RotateDrawable rotateDrawable = (RotateDrawable) spinner.getIndeterminateDrawable();

        // Access the GradientDrawable inside RotateDrawable
        GradientDrawable gradientDrawable = (GradientDrawable) rotateDrawable.getDrawable();

        // Update the end color dynamically
        int newEndColor = ContextCompat.getColor(this, R.color.white); // Use ContextCompat.getColor()
        gradientDrawable.setColors(new int[]{gradientDrawable.getColors()[0], newEndColor}); // Update the end color
        // Ensure this matches your XML layout filename

        new Handler().postDelayed(() -> {
            Intent mainIntent = new Intent(SG_Splash_Screen.this, SG_GameActivity.class);
            startActivity(mainIntent);
            finish();
        }, SPLASH_DISPLAY_LENGTH);
    }
}