package com.example.appsneakerstore.ui.screens

import androidx.compose.foundation.layout.*
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    userViewModel: UserViewModel = viewModel(),
    onLogin: () -> Unit,
    onGuestLogin: () -> Unit,
    onRegisterClick: () -> Unit
) {
    val usernameState by userViewModel.username.collectAsState()
    val errorState by userViewModel.loginError.collectAsState()

    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    LaunchedEffect(usernameState) {
        if (usernameState != null) {
            onLogin()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Sneaker Store") })
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
                onValueChange = { username.value = it },
                label = { Text("Usuario") },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                isError = errorState != null,
                shape = MaterialTheme.shapes.medium
            )
            errorState?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { userViewModel.login(username.value, password.value) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("INICIAR SESIÓN", style = MaterialTheme.typography.labelLarge)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                TextButton(onClick = onRegisterClick) {
                    Text("CREAR CUENTA")
                }
                Spacer(modifier = Modifier.weight(1f))
                TextButton(onClick = onGuestLogin) {
                    Text("ENTRAR COMO INVITADO")
                }
            }
        }
    }
}