package com.example.appsneakerstore.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.appsneakerstore.model.Order
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extension para crear DataStore
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class SessionManager(private val context: Context) {
    
    private val gson = Gson()
    
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
    
    // Obtener órdenes (dependientes del usuario)
    fun getOrdersForUser(username: String): Flow<List<Order>> {
        val key = stringPreferencesKey("orders_$username")
        return context.dataStore.data.map { preferences ->
            val json = preferences[key]
            if (json != null) {
                try {
                    val type = object : TypeToken<List<Order>>() {}.type
                    gson.fromJson(json, type)
                } catch (e: Exception) {
                    emptyList()
                }
            } else {
                emptyList()
            }
        }
    }
    
    // Guardar orden para un usuario específico
    suspend fun saveOrderForUser(username: String, newOrder: Order) {
        val key = stringPreferencesKey("orders_$username")
        context.dataStore.edit { preferences ->
            val currentJson = preferences[key]
            val currentOrders: MutableList<Order> = if (currentJson != null) {
                try {
                    val type = object : TypeToken<List<Order>>() {}.type
                    gson.fromJson(currentJson, type)
                } catch (e: Exception) {
                    mutableListOf()
                }
            } else {
                mutableListOf()
            }
            currentOrders.add(newOrder)
            preferences[key] = gson.toJson(currentOrders)
        }
    }
}
