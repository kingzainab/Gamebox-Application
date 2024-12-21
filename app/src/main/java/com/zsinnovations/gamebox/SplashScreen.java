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

        int SPLASH_DISPLAY_LENGTH = 3000;

        new Handler().postDelayed(() -> {

            Intent mainIntent = new Intent(SplashScreen.this, SplashActivity.class);
            startActivity(mainIntent);
            finish();
        }, SPLASH_DISPLAY_LENGTH);
    }
}
