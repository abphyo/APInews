package com.example.news.presentation.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class DataStoreManager(context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private val dataStore = context.dataStore

    companion object PreferenceKeys {
        val LIST_KEY = stringSetPreferencesKey("STRING_LIST_KEY")
    }

    suspend fun setStringList(list: MutableList<String>) {
        dataStore.edit { pref ->
            pref[LIST_KEY] = list.toSet()
        }
    }

    fun getStringList(): Flow<MutableList<String>> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) emit(emptyPreferences())
                else throw exception
            }.map { pref ->
                val set = pref[LIST_KEY] ?: emptySet()
                set.toMutableList()
            }
    }
}