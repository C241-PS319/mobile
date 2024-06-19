package com.c241ps319.patera.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.c241ps319.patera.data.model.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class DataStoreManager private constructor(private val dataStore: DataStore<Preferences>) {
    private val NAME_KEY = stringPreferencesKey("name")
    private val EMAIL_KEY = stringPreferencesKey("email")
    private val PICTURE_KEY = stringPreferencesKey("picture")
    private val PHONE_KEY = stringPreferencesKey("phone")
    private val TOKEN_KEY = stringPreferencesKey("token")
    private val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")

    suspend fun saveSession(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = user.name
            preferences[EMAIL_KEY] = user.email
            preferences[PICTURE_KEY] = user.picture ?: ""
            preferences[PHONE_KEY] = user.phone ?: ""
            preferences[TOKEN_KEY] = user.token
            preferences[IS_LOGIN_KEY] = true
        }
    }

    val session: Flow<UserModel?> = dataStore.data.map { preferences ->
        if (preferences[IS_LOGIN_KEY] == false || preferences[IS_LOGIN_KEY] == null) {
            null
        } else {
            val name = preferences[NAME_KEY] ?: ""
            val email = preferences[EMAIL_KEY] ?: ""
            val picture = preferences[PICTURE_KEY] ?: ""
            val phone = preferences[PHONE_KEY] ?: ""
            val token = preferences[TOKEN_KEY] ?: ""
            UserModel(name, email, picture, phone, token, true)
        }
    }

    suspend fun clearData() {
        dataStore.edit { preferences ->
            preferences.clear()
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