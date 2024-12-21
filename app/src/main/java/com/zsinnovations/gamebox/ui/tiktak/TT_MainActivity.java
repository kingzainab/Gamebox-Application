package com.zsinnovations.gamebox.ui.tiktak;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.zsinnovations.gamebox.R;
// Import the correct binding class
import com.zsinnovations.gamebox.databinding.ActivityTtMainBinding;
import com.zsinnovations.gamebox.ui.balloonburst.BG_GameActivity;
import com.zsinnovations.gamebox.ui.balloonburst.BG_MainActivity;

import java.util.ArrayList;
import java.util.List;

public class TT_MainActivity extends AppCompatActivity {

    // Declare binding with correct type
    private ActivityTtMainBinding binding;
    private final List<int[]> combinationList = new ArrayList<>();
    private int[] boxPositions = {0,0,0,0,0,0,0,0,0};
    private int playerTurn = 1;
    private int totalSelectedBoxes = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize binding correctly
        binding = ActivityTtMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        combinationList.add(new int[] {0,1,2});
        combinationList.add(new int[] {3,4,5});
        combinationList.add(new int[] {6,7,8});
        combinationList.add(new int[] {0,3,6});
        combinationList.add(new int[] {1,4,7});
        combinationList.add(new int[] {2,5,8});
        combinationList.add(new int[] {2,4,6});
        combinationList.add(new int[] {0,4,8});

        String getPlayerOneName = getIntent().getStringExtra("playerOne");
        String getPlayerTwoName = getIntent().getStringExtra("playerTwo");

        binding.playerOneName.setText(getPlayerOneName);
        binding.playerTwoName.setText(getPlayerTwoName);

        // Simplified click listeners using lambda expressions
        binding.image1.setOnClickListener(view -> {
            if (isBoxSelectable(0)) {
                performAction((ImageView) view, 0);
            }
        });

        binding.image2.setOnClickListener(view -> {
            if (isBoxSelectable(1)) {
                performAction((ImageView) view, 1);
            }
        });

        binding.image3.setOnClickListener(view -> {
            if (isBoxSelectable(2)) {
                performAction((ImageView) view, 2);
            }
        });

        binding.image4.setOnClickListener(view -> {
            if (isBoxSelectable(3)) {
                performAction((ImageView) view, 3);
            }
        });

        binding.image5.setOnClickListener(view -> {
            if (isBoxSelectable(4)) {
                performAction((ImageView) view, 4);
            }
        });

        binding.image6.setOnClickListener(view -> {
            if (isBoxSelectable(5)) {
                performAction((ImageView) view, 5);
            }
        });

        binding.image7.setOnClickListener(view -> {
            if (isBoxSelectable(6)) {
                performAction((ImageView) view, 6);
            }
        });

        binding.image8.setOnClickListener(view -> {
            if (isBoxSelectable(7)) {
                performAction((ImageView) view, 7);
            }
        });

        binding.image9.setOnClickListener(view -> {
            if (isBoxSelectable(8)) {
                performAction((ImageView) view, 8);
            }
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showExitConfirmationDialog();
            }
        });
    }

    private void showExitConfirmationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tic-Tac-Toe");
        builder.setMessage("Are you sure you want to quit the game?");
        builder.setCancelable(false);

        builder.setNegativeButton("Quit", (dialog, which) -> {
            Intent intent = new Intent(TT_MainActivity.this, TT_AddPlayer.class);
            startActivity(intent);
            finish();
        });

        builder.setPositiveButton("Resume", (dialog, which) -> {

        });
        builder.show();
    }

    private void performAction(ImageView imageView, int selectedBoxPosition) {
        boxPositions[selectedBoxPosition] = playerTurn;

        if (playerTurn == 1) {
            imageView.setImageResource(R.drawable.ximage);
            if (checkResults()) {
                TT_ResultDialog resultDialog = new TT_ResultDialog(TT_MainActivity.this,
                        binding.playerOneName.getText().toString() + " is a Winner!",
                        TT_MainActivity.this);
                resultDialog.setCancelable(false);
                resultDialog.show();
            } else if(totalSelectedBoxes == 9) {
                TT_ResultDialog resultDialog = new TT_ResultDialog(TT_MainActivity.this,
                        "Match Draw",
                        TT_MainActivity.this);
                resultDialog.setCancelable(false);
                resultDialog.show();
            } else {
                changePlayerTurn(2);
                totalSelectedBoxes++;
            }
        } else {
            imageView.setImageResource(R.drawable.oimage);
            if (checkResults()) {
                TT_ResultDialog resultDialog = new TT_ResultDialog(TT_MainActivity.this,
                        binding.playerTwoName.getText().toString() + " is a Winner!",
                        TT_MainActivity.this);
                resultDialog.setCancelable(false);
                resultDialog.show();
            } else if(totalSelectedBoxes == 9) {
                // Fixed incorrect dialog instantiation
                TT_ResultDialog resultDialog = new TT_ResultDialog(TT_MainActivity.this,
                        "Match Draw",
                        TT_MainActivity.this);
                resultDialog.setCancelable(false);
                resultDialog.show();
            } else {
                changePlayerTurn(1);
                totalSelectedBoxes++;
            }
        }
    }

    private void changePlayerTurn(int currentPlayerTurn) {
        playerTurn = currentPlayerTurn;
        if (playerTurn == 1) {
            binding.playerOneLayout.setBackgroundResource(R.drawable.black_border);
            binding.playerTwoLayout.setBackgroundResource(R.drawable.white_box);
        } else {
            binding.playerTwoLayout.setBackgroundResource(R.drawable.black_border);
            binding.playerOneLayout.setBackgroundResource(R.drawable.white_box);
        }
    }

    private boolean checkResults() {
        boolean response = false;
        for (int[] combination : combinationList) {
            if (boxPositions[combination[0]] == playerTurn &&
                    boxPositions[combination[1]] == playerTurn &&
                    boxPositions[combination[2]] == playerTurn) {
                response = true;
                break;
            }
        }
        return response;
    }

    private boolean isBoxSelectable(int boxPosition) {
        return boxPositions[boxPosition] == 0;
    }

    public void restartMatch() {
        boxPositions = new int[] {0,0,0,0,0,0,0,0,0};
        playerTurn = 1;
        totalSelectedBoxes = 1;

        binding.image1.setImageResource(R.drawable.white_box);
        binding.image2.setImageResource(R.drawable.white_box);
        binding.image3.setImageResource(R.drawable.white_box);
        binding.image4.setImageResource(R.drawable.white_box);
        binding.image5.setImageResource(R.drawable.white_box);
        binding.image6.setImageResource(R.drawable.white_box);
        binding.image7.setImageResource(R.drawable.white_box);
        binding.image8.setImageResource(R.drawable.white_box);
        binding.image9.setImageResource(R.drawable.white_box);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up binding
        binding = null;
    }
}