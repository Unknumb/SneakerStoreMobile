package com.example.appsneakerstore.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.ui.res.painterResource
import com.example.appsneakerstore.R
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appsneakerstore.model.User
import com.example.appsneakerstore.viewmodel.UserViewModel
import kotlinx.coroutines.launch

// Funciones de validación
private fun isValidPassword(password: String): Boolean {
    return password.length >= 6
}

private fun hasNumber(password: String): Boolean {
    return password.any { it.isDigit() }
}

private fun hasNoSpaces(text: String): Boolean {
    return !text.contains(" ")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    userViewModel: UserViewModel = viewModel(),
    onRegister: () -> Unit,
    onBack: () -> Unit
) {
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val registrationSuccess by userViewModel.registrationSuccess.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    
    // Estados de error de validación
    val usernameError = remember { mutableStateOf<String?>(null) }
    val passwordError = remember { mutableStateOf<String?>(null) }
    val confirmPasswordError = remember { mutableStateOf<String?>(null) }
    
    // Función para validar campos
    fun validateFields(): Boolean {
        var isValid = true
        
        // Validar usuario
        if (username.value.isBlank()) {
            usernameError.value = "El usuario es requerido"
            isValid = false
        } else if (username.value.length < 3) {
            usernameError.value = "Mínimo 3 caracteres"
            isValid = false
        } else if (!hasNoSpaces(username.value)) {
            usernameError.value = "No puede contener espacios"
            isValid = false
        } else {
            usernameError.value = null
        }
        
        // Validar contraseña
        if (password.value.isBlank()) {
            passwordError.value = "La contraseña es requerida"
            isValid = false
        } else if (!isValidPassword(password.value)) {
            passwordError.value = "Mínimo 6 caracteres"
            isValid = false
        } else if (!hasNumber(password.value)) {
            passwordError.value = "Debe contener al menos 1 número"
            isValid = false
        } else {
            passwordError.value = null
        }
        
        // Validar confirmación
        if (confirmPassword.value.isBlank()) {
            confirmPasswordError.value = "Confirma tu contraseña"
            isValid = false
        } else if (password.value != confirmPassword.value) {
            confirmPasswordError.value = "Las contraseñas no coinciden"
            isValid = false
        } else {
            confirmPasswordError.value = null
        }
        
        return isValid
    }

    LaunchedEffect(registrationSuccess) {
        if (registrationSuccess) {
            scope.launch {
                snackbarHostState.showSnackbar("Usuario registrado correctamente")
            }
            userViewModel.clearRegistrationSuccess()
            onRegister()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.logosn),
                            contentDescription = "Logo",
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Registro")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logosn),
                contentDescription = "Logo",
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("CREAR CUENTA", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(32.dp))
            OutlinedTextField(
                value = username.value,
                onValueChange = { 
                    username.value = it
                    usernameError.value = null
                },
                label = { Text("Usuario") },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                isError = usernameError.value != null,
                supportingText = {
                    usernameError.value?.let { error ->
                        Text(error, color = MaterialTheme.colorScheme.error)
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = password.value,
                onValueChange = { 
                    password.value = it
                    passwordError.value = null
                },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                isError = passwordError.value != null,
                supportingText = {
                    if (passwordError.value != null) {
                        Text(passwordError.value!!, color = MaterialTheme.colorScheme.error)
                    } else {
                        Text("Mínimo 6 caracteres y 1 número", color = MaterialTheme.colorScheme.outline)
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = confirmPassword.value,
                onValueChange = { 
                    confirmPassword.value = it
                    confirmPasswordError.value = null
                },
                label = { Text("Confirmar contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                isError = confirmPasswordError.value != null,
                supportingText = {
                    confirmPasswordError.value?.let { error ->
                        Text(error, color = MaterialTheme.colorScheme.error)
                    }
                }
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    if (validateFields()) {
                        userViewModel.register(User(username = username.value, password = password.value))
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("REGISTRARSE", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}