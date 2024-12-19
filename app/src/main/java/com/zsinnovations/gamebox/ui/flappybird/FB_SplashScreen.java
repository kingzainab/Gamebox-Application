package com.zsinnovations.gamebox.ui.flappybird;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.zsinnovations.gamebox.R;
import com.zsinnovations.gamebox.ui.balloonburst.BG_MainActivity;
import com.zsinnovations.gamebox.ui.balloonburst.BG_SplashScreen;

public class FB_SplashScreen extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_LENGTH = 3000; // 3 seconds
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fb_splash_screen);


        new Handler().postDelayed(() -> {

            Intent mainIntent = new Intent(FB_SplashScreen.this, FB_MainActivity.class);
            startActivity(mainIntent);
            finish();
        }, SPLASH_DISPLAY_LENGTH);
    }
}