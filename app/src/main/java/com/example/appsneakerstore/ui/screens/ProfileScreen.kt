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
import com.example.appsneakerstore.model.Order
import com.example.appsneakerstore.ui.components.AppBottomBar
import com.example.appsneakerstore.viewmodel.ProductViewModel
import com.example.appsneakerstore.viewmodel.UserViewModel
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userViewModel: UserViewModel,
    productViewModel: ProductViewModel,
    onLoginRedirect: () -> Unit,
    onRegisterClick: () -> Unit = {},
    onBack: () -> Unit,
    onHomeClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onCartClick: () -> Unit = {}
) {
    val username by userViewModel.username.collectAsState()
    val orders by userViewModel.orders.collectAsState()
    val clpFormat = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CL"))

    Scaffold(
        bottomBar = {
            AppBottomBar(
                currentRoute = "profile",
                onHomeClick = onHomeClick,
                onSearchClick = onSearchClick,
                onCartClick = onCartClick,
                onProfileClick = { /* Ya estamos en profile */ },
                productViewModel = productViewModel
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (username != null) {
                Text("MI CUENTA", style = MaterialTheme.typography.headlineLarge)
                Spacer(modifier = Modifier.height(32.dp))
                Text("Hola, $username", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(32.dp))
                Text("MIS COMPRAS", style = MaterialTheme.typography.headlineSmall)
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(orders) { order: Order ->
                        Column(modifier = Modifier.padding(vertical = 8.dp)) {
                            Text("Pedido del ${order.orderDate}", style = MaterialTheme.typography.titleMedium)
                            order.items.forEach {
                                ListItem(
                                    headlineContent = { Text(it.name, fontWeight = FontWeight.Bold) },
                                    supportingContent = { Text(clpFormat.format(it.price)) }
                                )
                            }
                            Text("Total: ${clpFormat.format(order.total)}", modifier = Modifier.align(Alignment.End))
                            HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
                        }
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
                    Text("Mi Cuenta", style = MaterialTheme.typography.headlineLarge)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Inicia sesión para ver tu perfil y tus compras.", 
                         style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = onLoginRedirect,
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
    }
}