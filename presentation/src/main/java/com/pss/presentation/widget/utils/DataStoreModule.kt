package com.pss.presentation.widget.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.pss.presentation.widget.utils.DataStore.DEFAULT_LOCATION
import com.pss.presentation.widget.utils.DataStore.PREFERENCE_LOCATION
import com.pss.presentation.widget.utils.DataStore.PREFERENCE_NAME
import com.pss.presentation.widget.utils.DataStoreModule.PreferencesKeys.dataStoreLocation
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(PREFERENCE_NAME)

class DataStoreModule @Inject constructor(@ApplicationContext private val context: Context){

    private object PreferencesKeys {
        val dataStoreLocation = stringPreferencesKey(PREFERENCE_LOCATION)
    }

    //private val intKey = intPreferencesKey("key_name") // int 저장 키값
    private val dataStore: DataStore<Preferences> =
        context.dataStore


    //쓰기
    suspend fun setLocation(text: String) {
        context.dataStore.edit { preferences ->
            preferences[dataStoreLocation] = text
        }
    }

    // 읽기
    val readLocation: Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[dataStoreLocation] ?: DEFAULT_LOCATION
        }
}