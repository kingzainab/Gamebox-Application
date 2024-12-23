package com.zsinnovations.gamebox.ui.TZFE;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import com.zsinnovations.gamebox.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class tzfe_GameActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {
    private LinearLayout scoreLayout;
    private LinearLayout highScoreLayout;
    private TextView scoreTextView;
    private TextView highScoreTextView;
    private long score;
    private long highScore;
    private final int UP = 0;
    private final int DOWN = 1;
    private final int LEFT = 2;
    private final int RIGHT = 3;
    private GestureDetectorCompat gestureDetectorCompat;
    private TextView[][] cellTextViewMatrix;
    private int[][] cellValueMatrix;
    ArrayList<Pair<Integer, Integer>> blankPairs;
    HashMap<Integer, Integer> correspondingColor;
    Button playAgainButton;
    boolean isGame;
    SharedPreferences sharedPreferences;

    public void reset(View v) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                cellValueMatrix[i][j] = 0;
                fillCellTextView(cellTextViewMatrix[i][j], cellValueMatrix[i][j]);
                blankPairs.add(new Pair<>(i, j));
            }
        }
        isGame = true;
        score = 0;
        scoreTextView.setText("" + score);
        highScore = sharedPreferences.getLong("highScore", 0);
        highScoreTextView.setText("" + highScore);
        fillRandomNo();
        fillRandomNo();
    }



    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }
}