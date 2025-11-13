package com.example.appsneakerstore.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appsneakerstore.viewmodel.UserViewModel

@Composable
fun ProfileScreen(userViewModel: UserViewModel = viewModel(), onLoginRedirect: () -> Unit) {
    val username by userViewModel.username.collectAsState()
    val purchases by userViewModel.purchases.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (username != null) {
            Text("Perfil de Usuario", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Usuario: $username", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                userViewModel.logout()
                onLoginRedirect()
            }) {
                Text("Cerrar sesión")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Historial de Compras", style = MaterialTheme.typography.headlineSmall)
            LazyColumn {
                items(purchases) { product ->
                    ListItem(
                        headlineContent = { Text(product.name) },
                        supportingContent = { Text("Precio: ${product.price}") }
                    )
                    HorizontalDivider()
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("No has iniciado sesión", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onLoginRedirect) {
                    Text("Iniciar sesión")
                }
            }
        }
    }
}