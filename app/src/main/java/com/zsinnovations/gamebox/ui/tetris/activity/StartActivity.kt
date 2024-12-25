package com.zsinnovations.gamebox.ui.tetris.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import com.zsinnovations.gamebox.MainActivity
import com.zsinnovations.gamebox.R
import com.zsinnovations.gamebox.databinding.TetrisActivityStartBinding

class StartActivity : AppCompatActivity() {
    private lateinit var binding: TetrisActivityStartBinding
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var sharedPreferences: SharedPreferences
    private var musicEnabled: Boolean = true // Default value for music setting

    companion object {
        private const val PREF_NAME = "tetris_settings"
        private const val KEY_MUSIC_ENABLED = "music_enabled"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TetrisActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        // Load the music enabled state from preferences
        musicEnabled = sharedPreferences.getBoolean(KEY_MUSIC_ENABLED, true)

        // Initialize MediaPlayer
        mediaPlayer = MediaPlayer.create(this, R.raw.tetris_theme_music) // Replace with your music file
        mediaPlayer?.isLooping = true // Loop the music
        if (musicEnabled) {
            startMusic()
            binding.soundOnIcon.visibility = View.VISIBLE
            binding.soundOffIcon.visibility = View.INVISIBLE
        } else {
            binding.soundOnIcon.visibility = View.INVISIBLE
            binding.soundOffIcon.visibility = View.VISIBLE
        }

        binding.soundOnIcon.setOnClickListener {
            updateMusicState(false)
        }

        binding.soundOffIcon.setOnClickListener {
            updateMusicState(true)
        }

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

    override fun onResume() {
        super.onResume()
        // Start music if it was enabled and was not playing
        if (musicEnabled && (mediaPlayer?.isPlaying != true)) {
            startMusic()
        }

    }

    override fun onPause() {
        super.onPause()
        // Pause the music when the activity is paused to prevent playing when not in use
        pauseMusic()
    }


    override fun onDestroy() {
        super.onDestroy()
        releaseMusic()
    }

    private fun startMusic() {
        if (mediaPlayer != null && !mediaPlayer!!.isPlaying) {
            mediaPlayer?.start()
        }
    }

    private fun pauseMusic() {
        if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
            mediaPlayer?.pause()
        }
    }

    private fun releaseMusic(){
        mediaPlayer?.release()
        mediaPlayer = null
    }

    // Call when music settings changed
    fun updateMusicState(isEnabled: Boolean) {
        musicEnabled = isEnabled

        with(sharedPreferences.edit()){
            putBoolean(KEY_MUSIC_ENABLED, isEnabled)
            apply()
        }

        if (musicEnabled) {
            startMusic()
            binding.soundOnIcon.visibility = View.VISIBLE
            binding.soundOffIcon.visibility = View.INVISIBLE

        }else{
            pauseMusic()
            binding.soundOnIcon.visibility = View.INVISIBLE
            binding.soundOffIcon.visibility = View.VISIBLE

        }
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
                navigateToStartActivity()
            }
            .setNegativeButton("No", null)
            .show()

    }
}