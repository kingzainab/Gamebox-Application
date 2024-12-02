package com.zsinnovations.gamebox.ui.balloonburst;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.gridlayout.widget.GridLayout;

import com.zsinnovations.gamebox.R;

import java.util.Random;

public class BG_GameActivity extends AppCompatActivity {

    private TextView textViewTime, textViewCountDown, textViewScore;
    private ImageView balloon1, balloon2, balloon3, balloon4, balloon5, balloon6, balloon7, balloon8, balloon9, mysteryBox, speakerLevelMax,speakerLevelMin,speakerZero;
    private GridLayout gridLayout;
    private MediaPlayer mediaPlayerPop, mediaPlayerMusic;
    private Handler handler;
    private Runnable runnable;
    private ImageView[] balloonsArray;
    private int score = 0;
    private boolean isClickable = false;
    private boolean isMysteryBoxVisible = false;
    private long timeRemaining = 30000; // 30 seconds initially
    private CountDownTimer gameTimer;
    private int[] balloonColors = {
            R.drawable.balloon_purple,
            R.drawable.balloon_pink,
            R.drawable.balloon_multi
    };
    private int soundState = 0; // 0: Unmute, 1: Mute Music, 2: Mute Pop

    private static final float VOLUME_MUSIC_ON = 1.0f;
    private static final float VOLUME_MUSIC_OFF = 0.0f;
    private static final float VOLUME_POP_ON = 1.0f;
    private static final float VOLUME_POP_OFF = 0.0f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bg_game);

        initializeViews();
        mediaPlayerPop = MediaPlayer.create(this, R.raw.balloon_pop);
        mediaPlayerMusic = MediaPlayer.create(this, R.raw.fb_audio);
        mediaPlayerMusic.setLooping(true);
        mediaPlayerMusic.start();

        balloonsArray = new ImageView[]{balloon1, balloon2, balloon3, balloon4, balloon5, balloon6, balloon7, balloon8, balloon9};

        startGameCountdown();
        setupSpeakerClickListener();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showExitConfirmationDialog();
            }
        });
    }

    private void setupSpeakerClickListener() {
        speakerLevelMax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleVolume();
            }
        });

        speakerLevelMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleVolume();
            }
        });

        speakerZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleVolume();
            }
        });
    }

    private void toggleVolume() {
        switch (soundState) {
            case 0: // All Unmuted -> Mute Music
                mediaPlayerMusic.setVolume(VOLUME_MUSIC_OFF, VOLUME_MUSIC_OFF); // Mute music
                mediaPlayerPop.setVolume(VOLUME_POP_ON, VOLUME_POP_ON);        // Keep pop sound unmuted
                updateSpeakerIcons(1);
                soundState = 1;
                break;

            case 1: // Music Muted -> Mute Pop Sound
                mediaPlayerMusic.setVolume(VOLUME_MUSIC_OFF, VOLUME_MUSIC_OFF); // Keep music muted
                mediaPlayerPop.setVolume(VOLUME_POP_OFF, VOLUME_POP_OFF);       // Mute pop sound
                updateSpeakerIcons(2);
                soundState = 2;
                break;

            case 2: // All Muted -> Unmute All
                mediaPlayerMusic.setVolume(VOLUME_MUSIC_ON, VOLUME_MUSIC_ON);   // Unmute music
                mediaPlayerPop.setVolume(VOLUME_POP_ON, VOLUME_POP_ON);         // Unmute pop sound
                updateSpeakerIcons(0);
                soundState = 0;
                break;
        }
    }

    private void updateSpeakerIcons(int state) {
        switch (state) {
            case 0: // Unmuted state
                speakerLevelMax.setVisibility(View.VISIBLE);
                speakerLevelMin.setVisibility(View.INVISIBLE);
                speakerZero.setVisibility(View.INVISIBLE);
                break;

            case 1: // Music muted state
                speakerLevelMax.setVisibility(View.INVISIBLE);
                speakerLevelMin.setVisibility(View.VISIBLE);
                speakerZero.setVisibility(View.INVISIBLE);
                break;

            case 2: // Pop muted state
                speakerLevelMax.setVisibility(View.INVISIBLE);
                speakerLevelMin.setVisibility(View.INVISIBLE);
                speakerZero.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void initializeViews() {
        textViewTime = findViewById(R.id.timeRemaining);
        textViewCountDown = findViewById(R.id.timer);
        textViewScore = findViewById(R.id.Ballonscore);
        balloon1 = findViewById(R.id.balloon1);
        balloon2 = findViewById(R.id.balloon2);
        balloon3 = findViewById(R.id.balloon3);
        balloon4 = findViewById(R.id.balloon4);
        balloon5 = findViewById(R.id.balloon5);
        balloon6 = findViewById(R.id.balloon6);
        balloon7 = findViewById(R.id.balloon7);
        balloon8 = findViewById(R.id.balloon8);
        balloon9 = findViewById(R.id.balloon9);
        speakerLevelMax=findViewById(R.id.level_two_sound);
        speakerLevelMin=findViewById(R.id.level_one_sound);
        speakerZero=findViewById(R.id.zero_sound);

        mysteryBox = findViewById(R.id.mysteryBox);
        gridLayout = findViewById(R.id.gridLayoutBalloon);
    }

    private void startGameCountdown() {
        new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                textViewCountDown.setVisibility(View.VISIBLE);
                textViewTime.setVisibility(View.INVISIBLE);
                textViewScore.setVisibility(View.INVISIBLE);
                gridLayout.setVisibility(View.INVISIBLE);

                textViewCountDown.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                startGameTimer();
                balloonControl();
            }
        }.start();
    }

    private void startGameTimer() {
        gameTimer = new CountDownTimer(timeRemaining, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemaining = millisUntilFinished;
                textViewTime.setText("Remaining Time : " + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(BG_GameActivity.this, BG_ResultActivity.class);
                intent.putExtra("score", score);
                startActivity(intent);
                finish();
            }
        }.start();
    }

    private void balloonControl() {
        textViewCountDown.setVisibility(View.INVISIBLE);
        textViewTime.setVisibility(View.VISIBLE);
        textViewScore.setVisibility(View.VISIBLE);
        gridLayout.setVisibility(View.VISIBLE);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                // Reset all balloons to default state
                for (ImageView balloon : balloonsArray) {
                    balloon.setVisibility(View.INVISIBLE); // Hide the balloon
                    balloon.setClickable(false); // Ensure it's not clickable when hidden
                }
                mysteryBox.setVisibility(View.INVISIBLE);

                // Randomly decide to show a balloon or mystery box
                Random random = new Random();
                boolean showMysteryBox = random.nextInt(5) == 0; // 20% chance for mystery box
                if (showMysteryBox) {
                    moveMysteryBoxRandomly();
                    isMysteryBoxVisible = true;
                } else {
                    int randomIndex = random.nextInt(balloonsArray.length);
                    int randomColorIndex = random.nextInt(balloonColors.length);
                    balloonsArray[randomIndex].setImageResource(balloonColors[randomColorIndex]); // Assign a random color
                    balloonsArray[randomIndex].setVisibility(View.VISIBLE);
                    balloonsArray[randomIndex].setClickable(true);
                    isMysteryBoxVisible = false;
                }

                isClickable = true;

                // Adjust delay based on score
                long delay = score <= 5 ? 1800 :
                        score <= 10 ? 1500 :
                                score <= 15 ? 1200 :
                                        score <= 22 ? 900 : 700;

                handler.postDelayed(this, delay);
            }
        };
        handler.post(runnable);
    }


    public void increaseScoreByOne(View view) {
        if (!isClickable) return;

        isClickable = false;

        if (view.getId() == R.id.mysteryBox) {
            handleMysteryBoxPop();
        } else {
            score++;
            textViewScore.setText("Score : " + score);

            if (mediaPlayerPop != null) {
                if (mediaPlayerPop.isPlaying()) mediaPlayerPop.seekTo(0);
                mediaPlayerPop.start();
            }

            for (ImageView balloon : balloonsArray) {
                if (view.getId() == balloon.getId()) {
                    balloon.setImageResource(R.drawable.boom); // Show "pop" image

                    // Reset balloon state after a delay
                    new Handler().postDelayed(() -> {
                        balloon.setImageResource(R.drawable.balloon_pink); // Reset to default image
                        balloon.setVisibility(View.INVISIBLE); // Hide the balloon
                    }, 500); // Delay long enough to show the "pop" animation

                    break;
                }
            }
        }
    }

    // New method to handle mystery box pop and display result
    private void handleMysteryBoxPop() {
        // Generate random outcome
        Random random = new Random();
        int outcome = random.nextInt(3); // Random number between 0 and 2

        // Display outcome text
        String resultMessage = "";
        switch (outcome) {
            case 0: // Add +10 score
                score += 10;
                mysteryBox.setImageResource(R.drawable.wow);
                resultMessage = "You got +10 points!";
                textViewScore.setText("Score : " + score);
                break;

            case 1: // Subtract -2 seconds from timer
                if (timeRemaining > 2000) timeRemaining -= 2000;
                mysteryBox.setImageResource(R.drawable.wow);

                resultMessage = "You lost 2 seconds!";
                textViewTime.setText("Remaining Time : " + timeRemaining / 1000);
                break;

            case 2: // Add +7 seconds to timer
                timeRemaining += 7000;
                mysteryBox.setImageResource(R.drawable.wow);
                resultMessage = "You gained 7 seconds!";
                textViewTime.setText("Remaining Time : " + timeRemaining / 1000);
                break;
        }

        // Display the result message on screen
        TextView outcomeTextView = findViewById(R.id.outcomeTextView);
        outcomeTextView.setText(resultMessage);  // Set the outcome message
        outcomeTextView.setVisibility(View.VISIBLE); // Show the TextView

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mysteryBox.setImageResource(R.drawable.mystery_box);
                outcomeTextView.setVisibility(View.GONE); // Hide the message after 2 seconds
            }
        }, 2000); // The message will disappear after 2 seconds
    }


    private void showExitConfirmationDialog() {
        if (handler != null) handler.removeCallbacks(runnable);
        if (gameTimer != null) gameTimer.cancel();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Balloon Burst");
        builder.setMessage("Are you sure you want to quit the game?");
        builder.setCancelable(false);

        builder.setNegativeButton("Quit", (dialog, which) -> {
            Intent intent = new Intent(BG_GameActivity.this, BG_MainActivity.class);
            startActivity(intent);
            finish();
        });

        builder.setPositiveButton("Resume", (dialog, which) -> balloonControl());
        builder.show();
    }

    private void moveMysteryBoxRandomly() {
        ConstraintLayout constraintLayout = findViewById(R.id.constraintLayoutBalloon);

        constraintLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                constraintLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                int layoutWidth = constraintLayout.getWidth();
                int layoutHeight = constraintLayout.getHeight();

                int boxWidth = mysteryBox.getWidth();
                int boxHeight = mysteryBox.getHeight();

                Random random = new Random();
                int randomX = random.nextInt(layoutWidth - boxWidth);
                int randomY = random.nextInt(layoutHeight - boxHeight);

                mysteryBox.setX(randomX);
                mysteryBox.setY(randomY);
                mysteryBox.setVisibility(View.VISIBLE);
            }
        });
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) handler.removeCallbacks(runnable);
        if (mediaPlayerMusic != null) mediaPlayerMusic.release();
        if (mediaPlayerPop != null) mediaPlayerPop.release();
    }
}
