package com.zsinnovations.gamebox.ui.tetris.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import androidx.lifecycle.lifecycleScope
import com.zsinnovations.gamebox.databinding.TetrisActivitySettingBinding
import com.zsinnovations.gamebox.ui.tetris.constants.BlockColorTheme
import com.zsinnovations.gamebox.ui.tetris.database.BlockThemeManager
import com.zsinnovations.gamebox.ui.tetris.database.LevelManager
import kotlinx.coroutines.launch

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: TetrisActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TetrisActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val blockThemeManager = BlockThemeManager(applicationContext)
        val levelManager = LevelManager(applicationContext)

        setupInitialLevel(levelManager)
        setupSeekBarListener(levelManager)
        setupInitialTheme(blockThemeManager)
        setupThemeListeners(blockThemeManager)
    }

    private fun setupInitialLevel(levelManager: LevelManager) {
        lifecycleScope.launch {
            val initialLevel = levelManager.getInitialLevel()
            binding.seekBar.progress = (initialLevel ?: 1) - 1
            binding.levelInstant.text = (initialLevel ?: 1).toString()
        }
    }

    private fun setupSeekBarListener(levelManager: LevelManager) {
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.levelInstant.text = (progress + 1).toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                lifecycleScope.launch {
                    seekBar?.let { levelManager.setInitialLevel(it.progress + 1) }  // Fixed typo here
                }
            }
        })
    }

    private fun setupInitialTheme(blockThemeManager: BlockThemeManager) {
        lifecycleScope.launch {
            when (blockThemeManager.getTheme()) {
                BlockColorTheme.THEME_MONALISA -> binding.radioTheme2.isChecked = true
                BlockColorTheme.THEME_BELAFONTE -> binding.radioTheme3.isChecked = true
                BlockColorTheme.THEME_ESPRESSO -> binding.radioTheme4.isChecked = true
                BlockColorTheme.THEME_SPECTRUM -> binding.radioTheme5.isChecked = true
                BlockColorTheme.THEME_RAINBOW -> binding.radioTheme6.isChecked = true
                else -> binding.radioTheme1.isChecked = true
            }
        }
    }

    private fun setupThemeListeners(blockThemeManager: BlockThemeManager) {
        binding.apply {
            radioTheme1.setOnClickListener { setTheme(blockThemeManager, BlockColorTheme.THEME_MODERN) }
            radioTheme2.setOnClickListener { setTheme(blockThemeManager, BlockColorTheme.THEME_MONALISA) }
            radioTheme3.setOnClickListener { setTheme(blockThemeManager, BlockColorTheme.THEME_BELAFONTE) }
            radioTheme4.setOnClickListener { setTheme(blockThemeManager, BlockColorTheme.THEME_ESPRESSO) }
            radioTheme5.setOnClickListener { setTheme(blockThemeManager, BlockColorTheme.THEME_SPECTRUM) }
            radioTheme6.setOnClickListener { setTheme(blockThemeManager, BlockColorTheme.THEME_RAINBOW) }
        }
    }

    private fun setTheme(blockThemeManager: BlockThemeManager, theme: String) {
        lifecycleScope.launch {
            blockThemeManager.setTheme(theme)
        }
    }
}