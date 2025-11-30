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
        private val FAVORITES_KEY = stringSetPreferencesKey("favorites")
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
    
    // Obtener favoritos
    val favorites: Flow<Set<Int>> = context.dataStore.data.map { preferences ->
        preferences[FAVORITES_KEY]?.map { it.toInt() }?.toSet() ?: emptySet()
    }
    
    // Guardar favoritos
    suspend fun saveFavorites(favoriteIds: Set<Int>) {
        context.dataStore.edit { preferences ->
            preferences[FAVORITES_KEY] = favoriteIds.map { it.toString() }.toSet()
        }
    }
    
    // Agregar/quitar favorito
    suspend fun toggleFavorite(productId: Int) {
        context.dataStore.edit { preferences ->
            val currentFavorites = preferences[FAVORITES_KEY]?.map { it.toInt() }?.toMutableSet() ?: mutableSetOf()
            if (currentFavorites.contains(productId)) {
                currentFavorites.remove(productId)
            } else {
                currentFavorites.add(productId)
            }
            preferences[FAVORITES_KEY] = currentFavorites.map { it.toString() }.toSet()
        }
    }
}
