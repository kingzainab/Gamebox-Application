package com.zsinnovations.gamebox.ui.tetris.database

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

class LevelManager(context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "INIT_LEVEL")
        val INITIAL_LEVEL = intPreferencesKey("init_level_key")
    }

    private val dataStore = context.dataStore

    suspend fun setInitialLevel(level: Int) {
        dataStore.edit { preferences ->
            preferences[INITIAL_LEVEL] = level
        }
    }

    suspend fun getInitialLevel(): Int? {
        return dataStore.data.first()[INITIAL_LEVEL]
    }
}
