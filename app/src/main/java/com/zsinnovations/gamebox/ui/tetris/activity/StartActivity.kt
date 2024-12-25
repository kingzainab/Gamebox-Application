package com.zsinnovations.gamebox.ui.tetris.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import com.zsinnovations.gamebox.MainActivity
import com.zsinnovations.gamebox.databinding.TetrisActivityStartBinding
import com.zsinnovations.gamebox.ui.mainscreen.GameFragment

class StartActivity : AppCompatActivity() {
    private lateinit var binding: TetrisActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TetrisActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.NewGameButton.setOnClickListener {
            val intent = Intent(this@StartActivity, Tetris_MainActivity::class.java)
            startActivity(intent)
        }

        binding.settingButton.setOnClickListener {
            val intent = Intent(this@StartActivity, SettingActivity::class.java)
            startActivity(intent)
        }

        binding.HighScoreButton.setOnClickListener {
            val intent = Intent(this@StartActivity, HighscoreActivity::class.java)
            startActivity(intent)
        }

        setupOnBackPressedDispatcher()
    }

    private fun setupOnBackPressedDispatcher() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitConfirmationDialog()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
    }
    private fun navigateToStartActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Confirm Exit")
            .setMessage("Are you sure you want to exit the application?")
            .setPositiveButton("Yes") { _, _ ->
                navigateToStartActivity() // Exit the app by finishing the activity
            }
            .setNegativeButton("No", null)
            .show()

    }
}