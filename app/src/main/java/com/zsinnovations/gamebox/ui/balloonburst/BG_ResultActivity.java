package com.zsinnovations.gamebox.ui.balloonburst;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.zsinnovations.gamebox.R;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.zsinnovations.gamebox.MainActivity;

public class BG_ResultActivity extends AppCompatActivity {

    private TextView textViewInfo, textViewScore, textViewHighestScore;
    private ImageView speaker;
    private boolean isMuted = false;
    int myScore;
    private MediaPlayer mediaPlayer;
    private Button buttonPlayAgain, buttonQuitGame;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bg_result);

        // Initialize views
        textViewInfo = findViewById(R.id.textViewInfo);
        textViewHighestScore = findViewById(R.id.textViewHighestScore);
        textViewScore = findViewById(R.id.textViewScoreResult);
        buttonQuitGame = findViewById(R.id.buttonQuitGame);
        buttonPlayAgain = findViewById(R.id.buttonPlayAgain);

        // Initialize MediaPlayer
        mediaPlayer = MediaPlayer.create(this, R.raw.level_end);
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }

        // Retrieve score from Intent
        myScore = getIntent().getIntExtra("score", 0);
        textViewScore.setText("Your Score : " + myScore);

        // SharedPreferences for storing highest score
        sharedPreferences = this.getSharedPreferences("BGGameScore", Context.MODE_PRIVATE);
        int highestScore = sharedPreferences.getInt("highestScore", 0);
        if (myScore >= highestScore) {
            sharedPreferences.edit().putInt("highestScore", myScore).apply();
            textViewHighestScore.setText("Highest Score : " + myScore);
            textViewInfo.setText("Congratulations! New High Score! Want to try again?");
        } else {
            textViewHighestScore.setText("Highest Score : " + highestScore);
            textViewInfo.setText(highestScore - myScore > 10 ? "You must get a little faster!" : "Better luck next time!");
        }

        // Play Again Button
        buttonPlayAgain.setOnClickListener(v -> {
            Intent intent = new Intent(BG_ResultActivity.this, BG_GameActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        // Quit Game Button
        buttonQuitGame.setOnClickListener(v -> {
            Intent intent = new Intent(BG_ResultActivity.this, BG_MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
