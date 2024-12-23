package com.zsinnovations.gamebox.ui.tetris.database

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.catch
import java.io.IOException

class BlockThemeManager(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "BLOCK_THEME")
        val BLOCK_THEME_NAME = stringPreferencesKey("block_theme_key")
    }

    private val dataStore = context.dataStore

    suspend fun setTheme(themeName: String) {
        try {
            dataStore.edit { preferences ->
                preferences[BLOCK_THEME_NAME] = themeName
            }
        } catch (e: IOException) {
            // Handle IO Exception
            e.printStackTrace()
        } catch (e: Exception) {
            // Handle other exceptions
            e.printStackTrace()
        }
    }

    suspend fun getTheme(): String? {
        return try {
            dataStore.data
                .catch { exception ->
                    if (exception is IOException) {
                        // Return an empty Preferences object if there's an IOException
                        emit(emptyPreferences())
                    } else {
                        throw exception
                    }
                }

                .first()[BLOCK_THEME_NAME]
        } catch (e: Exception) {
            // Handle exceptions and return null or a default value
            e.printStackTrace()
            null
        }
    }
}