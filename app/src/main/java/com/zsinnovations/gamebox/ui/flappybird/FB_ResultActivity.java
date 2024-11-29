//package com.zsinnovations.gamebox.ui.flappybird;
//
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//import com.zsinnovations.gamebox.R;
//
//public class FB_ResultActivity extends AppCompatActivity
//{
//    private TextView textViewResultInfo, textViewMyScore, textViewHighScore;
//    private Button buttonAgain;
//    private int score, highScore;
//
//    private SharedPreferences sharedPreferences;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_fb_result);
//
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//
//        textViewHighScore = findViewById(R.id.textViewHighScore);
//        textViewMyScore = findViewById(R.id.textViewMyScore);
//        textViewResultInfo = findViewById(R.id.textViewResultInfo);
//        buttonAgain = findViewById(R.id.buttonAgain);
//
//        // Get the score from the intent
//        score = getIntent().getIntExtra("score", 0);
//        textViewMyScore.setText("Score:" + score);
//
//        // Retrieve the high score from SharedPreferences
//        sharedPreferences = this.getSharedPreferences("Score", Context.MODE_PRIVATE);
//        sharedPreferences.getInt("highScore", 0);
//
//        // Update the high score and display appropriate messages
//        if (score >= 200)
//        {
//            textViewResultInfo.setText("You won the game :)");
//            textViewHighScore.setText("High Score: " + score);
//            sharedPreferences.edit().putInt("highScore", score).apply();
//        }
//        else if (score >= highScore)
//        {
//            textViewResultInfo.setText("Not your best game just yet!");
//            textViewHighScore.setText("High Score: " + score);
//            sharedPreferences.edit().putInt("highScore", score).apply();
//        }
//        else
//        {
//            textViewResultInfo.setText("Whoops, you lost the game.");
//            textViewHighScore.setText("High Score: " + highScore);
//        }
//
//        // Restart the game on button click
//        buttonAgain.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                Intent intent = new Intent(FB_ResultActivity.this, FB_GameActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });
//
//    }
//
//    @SuppressLint("MissingSuperCall")
//    public void onBackPressed()
//    {
//        AlertDialog.Builder builder = new AlertDialog.Builder(FB_ResultActivity.this);
//        builder.setTitle("Flappy Bird 🐥");
//        builder.setMessage("Are you sure you want to quit the game?");
//        builder.setCancelable(false);
//
//        builder.setNegativeButton("Quit", new DialogInterface.OnClickListener()
//        {
//            @Override
//            public void onClick(DialogInterface dialog, int which)
//            {
//                moveTaskToBack(true);
//                android.os.Process.killProcess(android.os.Process.myPid());
//                System.exit(0);
//            }
//        });
//
//        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener()
//        {
//            @Override
//            public void onClick(DialogInterface dialog, int which)
//            {
//                dialog.cancel();
//            }
//        });
//
//        builder.create().show();
//
//    }
//}
package com.zsinnovations.gamebox.ui.flappybird;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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

public class FB_ResultActivity extends AppCompatActivity {
    private TextView textViewResultInfo, textViewMyScore, textViewHighScore;
    private Button buttonAgain;
    private int score, highScore;

    private SharedPreferences sharedPreferences;

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

        // Get the score from the intent
        score = getIntent().getIntExtra("score", 0);
        textViewMyScore.setText("Score:" + score);


        // Retrieve and update high score
        sharedPreferences = this.getSharedPreferences("Score", Context.MODE_PRIVATE);
        highScore = sharedPreferences.getInt("highScore", 0);

        if (score >= 200) {
            textViewHighScore.setText("High Score: " + highScore);

            if (score > highScore) {
                highScore = score;
                sharedPreferences.edit().putInt("highScore", highScore).apply();
            }
        } else if (score > highScore) {
            textViewHighScore.setText("High Score: " + highScore);

            highScore = score;
            sharedPreferences.edit().putInt("highScore", highScore).apply();
        } else {
            textViewHighScore.setText("High Score: " + highScore);

        }

        textViewHighScore.setText("High Score: " + highScore);


        // Restart the game on button click
        buttonAgain.setOnClickListener(v -> {
            Intent intent = new Intent(FB_ResultActivity.this, FB_GameActivity.class);
            startActivity(intent);
            finish();
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showExitConfirmationDialog();
            }
        });
    }

    //    @Override
//    public void onBackPressed() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(FB_ResultActivity.this);
//        builder.setTitle("Flappy Bird 🐥");
//        builder.setMessage("Are you sure you want to quit the game?");
//        builder.setCancelable(false);
//
//        builder.setNegativeButton("Quit", (dialog, which) -> {
//            moveTaskToBack(true);
//            android.os.Process.killProcess(android.os.Process.myPid());
//            System.exit(0);
//        });
//
//        builder.setPositiveButton("Cancel", (dialog, which) -> dialog.cancel());
//        builder.setNeutralButton("Resume", (dialog, which) -> dialog.dismiss());
//        builder.create().show();
//    }
    private void showExitConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(FB_ResultActivity.this);
        builder.setTitle("Flappy Bird 🐥");
        builder.setMessage("Are you sure you want to quit the game?");
        builder.setCancelable(false);

        builder.setNegativeButton("Quit", (dialog, which) -> {
            onPause();
            Intent intent = new Intent(FB_ResultActivity.this, MainActivity.class); // Replace with your main activity class
            startActivity(intent);
            finish();
        });

        builder.setPositiveButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.create().show();
    }
}
