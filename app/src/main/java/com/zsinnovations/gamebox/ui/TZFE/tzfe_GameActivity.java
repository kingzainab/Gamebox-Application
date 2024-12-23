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

    boolean isGameOver() {
        if (cellValueMatrix[0][0] == 0)
            return false;
        for (int i = 0; i < 4; i++) {
            for (int j = 1; j < 4; j++) {
                if (cellValueMatrix[i][j] == 0 || cellValueMatrix[i][j] == cellValueMatrix[i][j - 1])
                    return false;
            }
        }
        for (int j = 0; j < 4; j++) {
            for (int i = 1; i < 4; i++) {
                if (cellValueMatrix[i][j] == 0 || cellValueMatrix[i][j] == cellValueMatrix[i - 1][j])
                    return false;
            }
        }
        return true;
    }

    void fillRandomNo() {
        if (blankPairs.isEmpty())
            return;
        Random random = new Random();
        int randomIndex = random.nextInt(blankPairs.size());
        Pair<Integer, Integer> randomBlankCell = blankPairs.get(randomIndex);
        int x = randomBlankCell.first;
        int y = randomBlankCell.second;
        blankPairs.remove(randomIndex);
        int fillValue = random.nextInt(2);
        if (fillValue == 0)
            cellValueMatrix[x][y] = 2;
        else
            cellValueMatrix[x][y] = 4;
        fillCellTextView(cellTextViewMatrix[x][y], cellValueMatrix[x][y]);
    }

    void fillCellTextView(TextView textView, int num) {
        if (num == 0)
            textView.setText(" ");
        else
            textView.setText("" + num);
        switch (num) {
            case 0:
                textView.setBackgroundColor(Color.LTGRAY);
                textView.setTextColor(Color.BLACK);
                break;
            case 2:
                textView.setBackgroundColor(Color.rgb(240, 240, 240));
                textView.setTextColor(Color.BLACK);
                break;
            case 4:
                textView.setBackgroundColor(Color.rgb(255, 255, 224));
                textView.setTextColor(Color.BLACK);
                break;
            case 8:
                textView.setBackgroundColor(Color.rgb(255, 200, 100));
                textView.setTextColor(Color.WHITE);
                break;
            case 16:
                textView.setBackgroundColor(Color.rgb(255, 140, 30));
                textView.setTextColor(Color.WHITE);
                break;
            case 32:
                textView.setBackgroundColor(Color.rgb(255, 100, 65));
                textView.setTextColor(Color.WHITE);
                break;
            case 64:
                textView.setBackgroundColor(Color.rgb(250, 80, 100));
                textView.setTextColor(Color.WHITE);
                break;
            case 128:
                textView.setBackgroundColor(Color.rgb(255, 220, 0));
                textView.setTextColor(Color.WHITE);
                break;
            case 256:
                textView.setBackgroundColor(Color.rgb(240, 240, 0));
                textView.setTextColor(Color.BLACK);
                break;
            case 512:
                textView.setBackgroundColor(Color.rgb(245, 245, 0));
                textView.setTextColor(Color.BLACK);
                break;
            case 1024:
                textView.setBackgroundColor(Color.rgb(250, 250, 0));
                textView.setTextColor(Color.BLACK);
                break;
            case 2048:
                textView.setBackgroundColor(Color.rgb(255, 255, 0));
                textView.setTextColor(Color.BLACK);
                break;
            default:
                textView.setBackgroundColor(Color.rgb(255, 255, 0));
                textView.setTextColor(Color.BLACK);
        }
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