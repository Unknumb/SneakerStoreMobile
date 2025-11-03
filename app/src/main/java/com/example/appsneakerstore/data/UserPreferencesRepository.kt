package com.example.appsneakerstore.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferencesRepository(private val context: Context) {
    
    companion object {
        private val USER_ID_KEY = stringPreferencesKey("user_id")
        private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
        private val USER_NAME_KEY = stringPreferencesKey("user_name")
        private val IS_LOGGED_IN_KEY = booleanPreferencesKey("is_logged_in")
        private val IS_GUEST_KEY = booleanPreferencesKey("is_guest")
    }

    val userIdFlow: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[USER_ID_KEY] }

    val userEmailFlow: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[USER_EMAIL_KEY] }

    val userNameFlow: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[USER_NAME_KEY] }

    val isLoggedInFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[IS_LOGGED_IN_KEY] ?: false }

    val isGuestFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[IS_GUEST_KEY] ?: false }

    suspend fun saveUserSession(userId: String, email: String, name: String, isGuest: Boolean = false) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userId
            preferences[USER_EMAIL_KEY] = email
            preferences[USER_NAME_KEY] = name
            preferences[IS_LOGGED_IN_KEY] = true
            preferences[IS_GUEST_KEY] = isGuest
        }
    }

    suspend fun clearUserSession() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
