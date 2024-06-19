package com.c241ps319.patera.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.c241ps319.patera.data.model.LoginResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class DataStoreManager private constructor(private val dataStore: DataStore<Preferences>) {
    private val NAME_KEY = stringPreferencesKey("name")
    private val USER_ID_KEY = stringPreferencesKey("userId")
    private val TOKEN_KEY = stringPreferencesKey("token")


    suspend fun saveLoginResult(loginResult: LoginResult) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = loginResult.name ?: ""
            preferences[USER_ID_KEY] = loginResult.userId ?: ""
            preferences[TOKEN_KEY] = loginResult.token ?: ""
        }
    }

    val loginResultFlow: Flow<LoginResult?> = dataStore.data.map { preferences ->
        val name = preferences[NAME_KEY]
        val userId = preferences[USER_ID_KEY]
        val token = preferences[TOKEN_KEY]
        if (name != null && userId != null && token != null) {
            LoginResult(
                name, userId, token
            )
        } else {
            null
        }

    }

    suspend fun clearData() {
        dataStore.edit {
            it.clear()
        }
    }


    companion object {
        @Volatile
        private var INSTANCE: DataStoreManager? = null

        fun getInstance(dataStore: DataStore<Preferences>): DataStoreManager {
            return INSTANCE ?: synchronized(this) {
                val instance = DataStoreManager(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}