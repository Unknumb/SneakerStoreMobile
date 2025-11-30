package com.example.appsneakerstore.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appsneakerstore.viewmodel.UserViewModel

// Funciones de validación
private fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

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
fun LoginScreen(
    userViewModel: UserViewModel = viewModel(),
    onLogin: () -> Unit,
    onRegisterClick: () -> Unit,
    onClose: () -> Unit
) {
    val usernameState by userViewModel.username.collectAsState()
    val errorState by userViewModel.loginError.collectAsState()

    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    
    // Estados de error de validación
    val usernameError = remember { mutableStateOf<String?>(null) }
    val passwordError = remember { mutableStateOf<String?>(null) }

    // Función para validar campos
    fun validateFields(): Boolean {
        var isValid = true
        
        // Validar usuario/email
        if (username.value.isBlank()) {
            usernameError.value = "El usuario es requerido"
            isValid = false
        } else if (!hasNoSpaces(username.value)) {
            usernameError.value = "El usuario no puede contener espacios"
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
        } else {
            passwordError.value = null
        }
        
        return isValid
    }

    LaunchedEffect(usernameState) {
        if (usernameState != null) {
            onLogin()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sneaker Store") },
                actions = {
                    IconButton(onClick = onClose) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar"
                        )
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("BIENVENIDO", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(32.dp))
            OutlinedTextField(
                value = username.value,
                onValueChange = { 
                    username.value = it
                    usernameError.value = null // Limpiar error al escribir
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
                    passwordError.value = null // Limpiar error al escribir
                },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                isError = passwordError.value != null || errorState != null,
                shape = MaterialTheme.shapes.medium,
                supportingText = {
                    when {
                        passwordError.value != null -> Text(passwordError.value!!, color = MaterialTheme.colorScheme.error)
                        errorState != null -> Text(errorState!!, color = MaterialTheme.colorScheme.error)
                    }
                }
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { 
                    if (validateFields()) {
                        userViewModel.login(username.value, password.value) 
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("INICIAR SESIÓN", style = MaterialTheme.typography.labelLarge)
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = onRegisterClick,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("CREAR CUENTA", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}