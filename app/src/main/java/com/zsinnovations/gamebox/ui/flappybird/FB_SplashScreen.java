package com.zsinnovations.gamebox.ui.flappybird;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.zsinnovations.gamebox.R;
import com.zsinnovations.gamebox.SplashActivity;
import com.zsinnovations.gamebox.SplashScreen;

public class FB_SplashScreen extends AppCompatActivity {

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb_splash_screen);

        // Delay for a few seconds before moving to MainActivity
        int SPLASH_DISPLAY_LENGTH = 3000; // 3000ms = 3 seconds

        new Handler().postDelayed(() -> {
            // Start MainActivity
            Intent mainIntent = new Intent(FB_SplashScreen.this, FB_MainActivity.class);
            startActivity(mainIntent);

            // Close the splash screen activity
            finish();
        }, SPLASH_DISPLAY_LENGTH);
    }
}