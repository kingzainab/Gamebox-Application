package com.zsinnovations.gamebox;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        // Delay for a few seconds before moving to MainActivity
        int SPLASH_DISPLAY_LENGTH = 3000; // 3000ms = 3 seconds

        new Handler().postDelayed(() -> {
            // Start MainActivity
            Intent mainIntent = new Intent(SplashScreen.this, SplashActivity.class);
            startActivity(mainIntent);

            // Close the splash screen activity
            finish();
        }, SPLASH_DISPLAY_LENGTH);
    }
}
