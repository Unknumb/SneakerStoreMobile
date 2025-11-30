package com.example.appsneakerstore.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.appsneakerstore.ui.components.AppBottomBar
import com.example.appsneakerstore.viewmodel.ProductViewModel
import com.example.appsneakerstore.viewmodel.UserViewModel
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    viewModel: ProductViewModel,
    userViewModel: UserViewModel = viewModel(),
    onBack: () -> Unit,
    onCheckout: () -> Unit,
    onHomeClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    val cartMap by viewModel.cartItems.collectAsState()
    val totalAmount = cartMap.entries.sumOf { it.key.price * it.value }
    val clpFormat = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CL"))
    val currentUser by userViewModel.username.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CARRITO") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        bottomBar = {
            AppBottomBar(
                currentRoute = "cart",
                onHomeClick = onHomeClick,
                onSearchClick = onSearchClick,
                onCartClick = { /* Ya estamos en cart */ },
                onProfileClick = onProfileClick,
                productViewModel = viewModel
            )
        }
    ) { paddingValues ->
        if (cartMap.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Tu carrito está vacío.", style = MaterialTheme.typography.titleLarge)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(cartMap.entries.toList()) { (product, quantity) ->
                        ListItem(
                            headlineContent = { Text(product.name, fontWeight = FontWeight.Bold) },
                            supportingContent = { Text("${clpFormat.format(product.price)} x $quantity") },
                            leadingContent = {
                                AsyncImage(
                                    model = product.imageUrl,
                                    contentDescription = product.name,
                                    modifier = Modifier.size(64.dp),
                                    contentScale = ContentScale.Crop
                                )
                            },
                            trailingContent = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(onClick = { viewModel.removeFromCart(product) }) {
                                        Icon(Icons.Default.Remove, contentDescription = "Restar uno")
                                    }
                                    Text(quantity.toString())
                                    IconButton(onClick = { viewModel.addToCart(product) }) {
                                        Icon(Icons.Default.Add, contentDescription = "Sumar uno")
                                    }
                                    IconButton(onClick = { viewModel.removeItemFromCart(product) }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                                    }
                                }
                            }
                        )
                        HorizontalDivider()
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total:", style = MaterialTheme.typography.headlineSmall)
                    Text(clpFormat.format(totalAmount), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onCheckout,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("COMPRAR", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}