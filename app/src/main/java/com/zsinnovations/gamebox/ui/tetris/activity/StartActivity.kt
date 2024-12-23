package com.zsinnovations.gamebox.ui.tetris.activity;

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zsinnovations.gamebox.ui.tetris.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


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


    }
}