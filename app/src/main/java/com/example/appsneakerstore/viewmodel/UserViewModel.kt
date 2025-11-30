package com.example.appsneakerstore.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsneakerstore.data.local.SessionManager
import com.example.appsneakerstore.data.repository.UserRepository
import com.example.appsneakerstore.model.Order
import com.example.appsneakerstore.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserRepository(application)
    private val sessionManager = SessionManager(application)

    private val _username = MutableStateFlow<String?>(null)
    val username: StateFlow<String?> = _username.asStateFlow()

    // Ahora usamos un mapa mutable para almacenar órdenes por usuario en memoria
    private val _allOrders = MutableStateFlow<Map<String, List<Order>>>(emptyMap())
    
    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    private val _favorites = MutableStateFlow<Set<Int>>(emptySet())
    val favorites: StateFlow<Set<Int>> = _favorites.asStateFlow()

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError.asStateFlow()

    private val _registrationSuccess = MutableStateFlow(false)
    val registrationSuccess: StateFlow<Boolean> = _registrationSuccess.asStateFlow()

    private val _hasShownLoginPopup = MutableStateFlow(false)
    val hasShownLoginPopup: StateFlow<Boolean> = _hasShownLoginPopup.asStateFlow()
    
    // Estado para mostrar aviso de "iniciar sesión"
    private val _showLoginPrompt = MutableStateFlow(false)
    val showLoginPrompt: StateFlow<Boolean> = _showLoginPrompt.asStateFlow()

    init {
        // Restaurar sesión al iniciar
        viewModelScope.launch {
            sessionManager.loggedInUsername.collectLatest { savedUsername ->
                _username.value = savedUsername
                if (savedUsername != null) {
                    _hasShownLoginPopup.value = true
                    loadUserData(savedUsername)
                } else {
                    // Limpiar datos inmediatamente si no hay usuario
                    _favorites.value = emptySet()
                    _orders.value = emptyList()
                }
            }
        }
    }
    
    private fun loadUserData(username: String) {
        // Cargar favoritos del usuario
        viewModelScope.launch {
            sessionManager.getFavoritesForUser(username).collect { userFavorites ->
                // Solo actualizamos si el usuario sigue siendo el mismo (para evitar condiciones de carrera al logout rápido)
                if (_username.value == username) {
                    _favorites.value = userFavorites
                }
            }
        }
        
        // Cargar órdenes del usuario (desde memoria por ahora)
        _orders.value = _allOrders.value[username] ?: emptyList()
    }

    fun markLoginPopupShown() {
        _hasShownLoginPopup.value = true
    }
    
    fun dismissLoginPrompt() {
        _showLoginPrompt.value = false
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            repository.getUserByUsername(username).collect { user ->
                if (user?.password == password) {
                    _loginError.value = null
                    sessionManager.saveSession(username)
                } else {
                    _loginError.value = "Usuario o contraseña incorrectos"
                }
            }
        }
    }

    fun register(user: User) {
        viewModelScope.launch {
            repository.insertUser(user)
            _registrationSuccess.value = true
        }
    }

    fun toggleFavorite(productId: Int) {
        val currentUser = _username.value
        
        if (currentUser == null) {
            _showLoginPrompt.value = true
            return
        }
        
        val currentFavorites = _favorites.value.toMutableSet()
        
        if (currentFavorites.contains(productId)) {
            currentFavorites.remove(productId)
        } else {
            currentFavorites.add(productId)
        }
        
        _favorites.value = currentFavorites
        
        viewModelScope.launch {
            sessionManager.saveFavoritesForUser(currentUser, currentFavorites)
        }
    }

    fun addOrder(order: Order) {
        val currentUser = _username.value ?: return
        
        // Actualizar mapa global de órdenes
        val currentMap = _allOrders.value.toMutableMap()
        val userOrders = (currentMap[currentUser] ?: emptyList()).toMutableList()
        userOrders.add(order)
        currentMap[currentUser] = userOrders
        _allOrders.value = currentMap
        
        // Actualizar estado actual observable
        _orders.value = userOrders
    }

    fun logout() {
        viewModelScope.launch {
            sessionManager.clearSession()
            // Forzar limpieza inmediata de estados en memoria
            _username.value = null
            _favorites.value = emptySet()
            _orders.value = emptyList()
        }
    }

    fun clearRegistrationSuccess() {
        _registrationSuccess.value = false
    }
}
