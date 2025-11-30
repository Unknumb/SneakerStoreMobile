package com.example.appsneakerstore.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extension para crear DataStore
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class SessionManager(private val context: Context) {
    
    companion object {
        private val USERNAME_KEY = stringPreferencesKey("logged_in_username")
    }
    
    // Obtener usuario logueado
    val loggedInUsername: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USERNAME_KEY]
    }
    
    // Guardar sesión
    suspend fun saveSession(username: String) {
        context.dataStore.edit { preferences ->
            preferences[USERNAME_KEY] = username
        }
    }
    
    // Cerrar sesión
    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.remove(USERNAME_KEY)
        }
    }
    
    // Obtener favoritos (dependientes del usuario)
    fun getFavoritesForUser(username: String): Flow<Set<Int>> {
        val key = stringSetPreferencesKey("favorites_$username")
        return context.dataStore.data.map { preferences ->
            preferences[key]?.map { it.toInt() }?.toSet() ?: emptySet()
        }
    }
    
    // Guardar favoritos para un usuario específico
    suspend fun saveFavoritesForUser(username: String, favoriteIds: Set<Int>) {
        val key = stringSetPreferencesKey("favorites_$username")
        context.dataStore.edit { preferences ->
            preferences[key] = favoriteIds.map { it.toString() }.toSet()
        }
    }
}
