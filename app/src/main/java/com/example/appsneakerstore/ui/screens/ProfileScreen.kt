package com.example.appsneakerstore.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appsneakerstore.viewmodel.UserViewModel
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userViewModel: UserViewModel = viewModel(),
    onLoginRedirect: () -> Unit,
    onBack: () -> Unit
) {
    val username by userViewModel.username.collectAsState()
    val purchases by userViewModel.purchases.collectAsState()
    val clpFormat = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CL"))

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp)
        ) {
            if (username != null) {
                Text("MI CUENTA", style = MaterialTheme.typography.headlineLarge)
                Spacer(modifier = Modifier.height(32.dp))
                Text("Hola, $username", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(32.dp))
                Text("MIS COMPRAS", style = MaterialTheme.typography.headlineSmall)
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(purchases) { product ->
                        ListItem(
                            headlineContent = { Text(product.name, fontWeight = FontWeight.Bold) },
                            supportingContent = { Text(clpFormat.format(product.price)) }
                        )
                        HorizontalDivider()
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(
                    onClick = {
                        userViewModel.logout()
                        onLoginRedirect()
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("CERRAR SESIÓN", style = MaterialTheme.typography.labelLarge)
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Únete a nosotros", style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Inicia sesión para ver tu perfil y tus compras.")
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onLoginRedirect,
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text("INICIAR SESIÓN", style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }
    }
}