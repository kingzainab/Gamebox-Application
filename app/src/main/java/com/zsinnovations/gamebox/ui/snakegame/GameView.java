package com.zsinnovations.gamebox.ui.snakegame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;



public class GameView extends View {
    private final Paint paint = new Paint();
    private SnakeGameViewModel viewModel;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setViewModel(SnakeGameViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (viewModel != null) {
            // Draw background more efficiently
            canvas.drawColor(Color.BLACK);

            // Separate paints for different elements
            Paint snakePaint = new Paint();
            snakePaint.setColor(Color.GREEN);
            snakePaint.setStyle(Paint.Style.FILL);

            Paint applePaint = new Paint();
            applePaint.setColor(Color.RED);
            applePaint.setStyle(Paint.Style.FILL);

            Paint textPaint = new Paint();
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize(50);

            // Draw snake
            for (int[] part : viewModel.getSnake()) {
                canvas.drawRect(
                        part[0] * 40, part[1] * 40,
                        (part[0] + 1) * 40, (part[1] + 1) * 40,
                        snakePaint
                );
            }

            // Draw apple
            int[] apple = viewModel.getApple();
            canvas.drawRect(
                    apple[0] * 40, apple[1] * 40,
                    (apple[0] + 1) * 40, (apple[1] + 1) * 40,
                    applePaint
            );

            // Draw score
            canvas.drawText("Score: " + viewModel.getScore(), 20, 50, textPaint);
        }
    }
}
