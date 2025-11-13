package com.example.appsneakerstore.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsneakerstore.data.repository.UserRepository
import com.example.appsneakerstore.model.Product
import com.example.appsneakerstore.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserRepository(application)

    private val _username = MutableStateFlow<String?>(null)
    val username: StateFlow<String?> = _username.asStateFlow()

    private val _purchases = MutableStateFlow<List<Product>>(emptyList())
    val purchases: StateFlow<List<Product>> = _purchases.asStateFlow()

    private val _favorites = MutableStateFlow<Set<Int>>(emptySet())
    val favorites: StateFlow<Set<Int>> = _favorites.asStateFlow()

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError.asStateFlow()

    private val _registrationSuccess = MutableStateFlow(false)
    val registrationSuccess: StateFlow<Boolean> = _registrationSuccess.asStateFlow()

    fun login(username: String, password: String) {
        viewModelScope.launch {
            repository.getUserByUsername(username).collect {
                if (it?.password == password) {
                    _username.value = username
                    _loginError.value = null
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
        val currentFavorites = _favorites.value.toMutableSet()
        if (currentFavorites.contains(productId)) {
            currentFavorites.remove(productId)
        } else {
            currentFavorites.add(productId)
        }
        _favorites.value = currentFavorites
    }

    fun addPurchase(product: Product) {
        val currentPurchases = _purchases.value.toMutableList()
        currentPurchases.add(product)
        _purchases.value = currentPurchases
    }

    fun logout() {
        _username.value = null
        _purchases.value = emptyList()
        _favorites.value = emptySet()
    }

    fun clearRegistrationSuccess() {
        _registrationSuccess.value = false
    }
}