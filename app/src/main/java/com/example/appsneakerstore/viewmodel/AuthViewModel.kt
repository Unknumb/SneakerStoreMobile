package com.example.appsneakerstore.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsneakerstore.data.UserPreferencesRepository
import com.example.appsneakerstore.data.UserRepository
import com.example.appsneakerstore.model.RegistrationData
import com.example.appsneakerstore.model.User
import com.example.appsneakerstore.model.UserCredentials
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val currentUser: User? = null,
    val isLoggedIn: Boolean = false,
    val errorMessage: String? = null
)

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository = UserRepository.getInstance()
    private val preferencesRepository = UserPreferencesRepository(application)

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        checkExistingSession()
    }

    private fun checkExistingSession() {
        viewModelScope.launch {
            preferencesRepository.isLoggedInFlow.collect { isLoggedIn ->
                if (isLoggedIn) {
                    val userId = preferencesRepository.userIdFlow
                    val email = preferencesRepository.userEmailFlow
                    val name = preferencesRepository.userNameFlow
                    val isGuest = preferencesRepository.isGuestFlow

                    // Collect values and create user
                    var userIdVal: String? = null
                    var emailVal: String? = null
                    var nameVal: String? = null
                    var isGuestVal = false

                    userId.collect { userIdVal = it }
                    email.collect { emailVal = it }
                    name.collect { nameVal = it }
                    isGuest.collect { isGuestVal = it }

                    if (userIdVal != null && emailVal != null && nameVal != null) {
                        _uiState.value = _uiState.value.copy(
                            currentUser = User(userIdVal!!, emailVal!!, nameVal!!, isGuestVal),
                            isLoggedIn = true
                        )
                    }
                }
            }
        }
    }

    fun login(email: String, password: String) {
        // Validate inputs
        val validationError = validateLoginInputs(email, password)
        if (validationError != null) {
            _uiState.value = _uiState.value.copy(errorMessage = validationError)
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            val result = userRepository.loginUser(UserCredentials(email, password))
            result.onSuccess { user ->
                preferencesRepository.saveUserSession(user.id, user.email, user.name, user.isGuest)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    currentUser = user,
                    isLoggedIn = true,
                    errorMessage = null
                )
            }.onFailure { exception ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = exception.message ?: "Error al iniciar sesión"
                )
            }
        }
    }

    fun register(registrationData: RegistrationData) {
        // Validate inputs
        val validationError = validateRegistrationInputs(registrationData)
        if (validationError != null) {
            _uiState.value = _uiState.value.copy(errorMessage = validationError)
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            val result = userRepository.registerUser(
                registrationData.name,
                registrationData.email,
                registrationData.password
            )

            result.onSuccess { user ->
                preferencesRepository.saveUserSession(user.id, user.email, user.name, user.isGuest)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    currentUser = user,
                    isLoggedIn = true,
                    errorMessage = null
                )
            }.onFailure { exception ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = exception.message ?: "Error al registrarse"
                )
            }
        }
    }

    fun continueAsGuest() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            val guestUser = userRepository.createGuestUser()
            preferencesRepository.saveUserSession(guestUser.id, guestUser.email, guestUser.name, true)
            
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                currentUser = guestUser,
                isLoggedIn = true,
                errorMessage = null
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            preferencesRepository.clearUserSession()
            _uiState.value = AuthUiState()
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    private fun validateLoginInputs(email: String, password: String): String? {
        if (email.isBlank()) {
            return "El correo electrónico es requerido"
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return "El correo electrónico no es válido"
        }
        if (password.isBlank()) {
            return "La contraseña es requerida"
        }
        if (password.length < 6) {
            return "La contraseña debe tener al menos 6 caracteres"
        }
        return null
    }

    private fun validateRegistrationInputs(data: RegistrationData): String? {
        if (data.name.isBlank()) {
            return "El nombre es requerido"
        }
        if (data.name.length < 2) {
            return "El nombre debe tener al menos 2 caracteres"
        }
        if (data.email.isBlank()) {
            return "El correo electrónico es requerido"
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(data.email).matches()) {
            return "El correo electrónico no es válido"
        }
        if (data.password.isBlank()) {
            return "La contraseña es requerida"
        }
        if (data.password.length < 6) {
            return "La contraseña debe tener al menos 6 caracteres"
        }
        if (data.password != data.confirmPassword) {
            return "Las contraseñas no coinciden"
        }
        return null
    }
}
