package com.zsinnovations.gamebox.ui.flappybird;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.zsinnovations.gamebox.MainActivity;
import com.zsinnovations.gamebox.R;
import com.zsinnovations.gamebox.ui.balloonburst.BG_GameActivity;
import com.zsinnovations.gamebox.ui.balloonburst.BG_MainActivity;
import com.zsinnovations.gamebox.ui.balloonburst.BG_ResultActivity;

public class FB_ResultActivity extends AppCompatActivity {
    private TextView textViewResultInfo, textViewMyScore, textViewHighScore;
    private Button buttonAgain,buttonHome;
    private int score, highScore;

    private SharedPreferences sharedPreferences;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fb_result);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textViewHighScore = findViewById(R.id.textViewHighScore);
        textViewMyScore = findViewById(R.id.textViewMyScore);
        textViewResultInfo = findViewById(R.id.textViewResultInfo);
        buttonAgain = findViewById(R.id.buttonAgain);
        buttonHome = findViewById(R.id.buttonHome);

        // Get the score from the intent
        score = getIntent().getIntExtra("score", 0);
        textViewMyScore.setText("Score:" + score);


        // Retrieve and update high score
        sharedPreferences = this.getSharedPreferences("Score", Context.MODE_PRIVATE);
        highScore = sharedPreferences.getInt("highScore", 0);

        if (score >= 200)
        {
            textViewResultInfo.setText("You won the game :)");
            textViewHighScore.setText("High Score: " + score);
            sharedPreferences.edit().putInt("highScore", score).apply();
        }
        else if (score >= highScore)
        {
            textViewResultInfo.setText("Not your best game just yet!");
            textViewHighScore.setText("High Score: " + score);
            sharedPreferences.edit().putInt("highScore", score).apply();
        }
        else
        {
            textViewResultInfo.setText("Whoops, you lost the game.");
            textViewHighScore.setText("High Score: " + highScore);
        }

        // Restart the game on button click
        buttonAgain.setOnClickListener(v -> {
            Intent intent = new Intent(FB_ResultActivity.this, FB_GameActivity.class);
            startActivity(intent);
            finish();
        });

//        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
//            @Override
//            public void handleOnBackPressed() {
//                showExitConfirmationDialog();
//            }
//        });

        buttonAgain.setOnClickListener(v -> {
            Intent intent = new Intent(FB_ResultActivity.this, FB_GameActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        // Quit Game Button
        buttonHome.setOnClickListener(v -> {
            Intent intent = new Intent(FB_ResultActivity.this, FB_MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
//    private void showExitConfirmationDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(FB_ResultActivity.this);
//        builder.setTitle("Flappy Bird 🐥");
//        builder.setMessage("Are you sure you want to quit the game?");
//        builder.setCancelable(false);
//
//        builder.setNegativeButton("Quit", (dialog, which) -> {
//            onPause();
//            Intent intent = new Intent(FB_ResultActivity.this, MainActivity.class); // Replace with your main activity class
//            startActivity(intent);
//            finish();
//        });
//
//        builder.setPositiveButton("Cancel", (dialog, which) -> dialog.cancel());
//
//        builder.create().show();
//    }
}
