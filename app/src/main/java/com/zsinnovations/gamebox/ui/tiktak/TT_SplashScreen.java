package com.zsinnovations.gamebox.ui.tiktak;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.zsinnovations.gamebox.R;
import com.zsinnovations.gamebox.ui.balloonburst.BG_MainActivity;
import com.zsinnovations.gamebox.ui.balloonburst.BG_SplashScreen;

public class TT_SplashScreen extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tt_splash_screen);

        new Handler().postDelayed(() -> {
            Intent mainIntent = new Intent(TT_SplashScreen.this, TT_AddPlayer.class);
            startActivity(mainIntent);
            finish();
        }, SPLASH_DISPLAY_LENGTH);
    }
}