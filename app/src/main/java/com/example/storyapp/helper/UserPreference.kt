package com.example.storyapp.helper

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.storyapp.data.model.login.UserSession
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class UserPreference @Inject constructor(@ApplicationContext val context: Context) {

    private val dataStore = context.dataStore

    fun getUser(): Flow<UserSession> {
        return dataStore.data.map { preferences ->
            UserSession(
                preferences[NAME_KEY] ?:"",
                preferences[TOKEN] ?:"",
                preferences[USER_ID_KEY] ?:"",
                preferences[LOGIN_STATE] ?: false
            )
        }
    }

    suspend fun setUser(user: UserSession) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = user.name
            preferences[TOKEN] = user.token
            preferences[USER_ID_KEY] = user.userId
            preferences[LOGIN_STATE] = user.isLogin
        }
    }


    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[LOGIN_STATE] = false
        }
    }

    companion object {

        private val NAME_KEY = stringPreferencesKey("name")
        private val TOKEN = stringPreferencesKey("email")
        private val USER_ID_KEY = stringPreferencesKey("password")
        private val LOGIN_STATE = booleanPreferencesKey("state")

    }
}