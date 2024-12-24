package com.zsinnovations.gamebox.ui.tetris.database

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore // Updated import
import kotlinx.coroutines.flow.first

class BlockThemeManager(private val context: Context) {
    // Define the DataStore at companion object level using property delegate
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "BLOCK_THEME")
        val BLOCK_THEME_NAME = stringPreferencesKey("block_theme_key")
    }

    suspend fun setTheme(themeName: String) {
        context.dataStore.edit { preferences ->
            preferences[BLOCK_THEME_NAME] = themeName
        }
    }

    suspend fun getTheme(): String? {
        return context.dataStore.data.first()[BLOCK_THEME_NAME]
    }
}