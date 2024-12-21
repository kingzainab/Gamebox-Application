package com.zsinnovations.gamebox.ui.balloonburst;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.zsinnovations.gamebox.R;

public class BG_SplashScreen extends AppCompatActivity {
    private static final int SPLASH_DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bg_splash_screen);

        new Handler().postDelayed(() -> {
            Intent mainIntent = new Intent(BG_SplashScreen.this, BG_MainActivity.class);
            startActivity(mainIntent);
            finish();
        }, SPLASH_DISPLAY_LENGTH);
    }
}