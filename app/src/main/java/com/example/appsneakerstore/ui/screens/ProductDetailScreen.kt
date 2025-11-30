package com.example.appsneakerstore.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.appsneakerstore.model.Product
import com.example.appsneakerstore.viewmodel.ProductViewModel
import com.example.appsneakerstore.viewmodel.UserViewModel
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    product: Product,
    viewModel: ProductViewModel,
    userViewModel: UserViewModel,
    onBack: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val selectedSize = remember { mutableStateOf<Int?>(null) }
    val clpFormat = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CL"))
    val username by userViewModel.username.collectAsState()
    val favorites by userViewModel.favorites.collectAsState()
    val isFavorite = favorites.contains(product.id)

    // Mostrar aviso de login requerido para favoritos
    LaunchedEffect(Unit) {
        userViewModel.showLoginPrompt.collect { 
            launch {
                snackbarHostState.showSnackbar(
                    message = "Debes iniciar sesión para agregar favoritos",
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { userViewModel.toggleFavorite(product.id) }) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Favorite"
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                Column {
                    AsyncImage(
                        model = product.imageUrl,
                        contentDescription = product.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp),
                        contentScale = ContentScale.Crop
                    )
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = product.name, style = MaterialTheme.typography.headlineLarge)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = clpFormat.format(product.price),
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Color: ${product.color}",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Stock disponible: ${product.stock}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (product.stock > 0) Color.Black else Color.Red
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = product.description,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Calificación:", style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.width(8.dp))
                            Row {
                                for (i in 1..5) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = if (i <= product.rating) Color.Yellow else Color.Gray
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Tallas:", style = MaterialTheme.typography.titleMedium)
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(product.sizes) { size ->
                                OutlinedButton(
                                    onClick = { selectedSize.value = size },
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = if (selectedSize.value == size) MaterialTheme.colorScheme.primary else Color.Transparent,
                                        contentColor = if (selectedSize.value == size) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                                    )
                                ) {
                                    Text(text = size.toString())
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = {
                                viewModel.addToCart(product)
                                scope.launch {
                                    snackbarHostState.showSnackbar("Añadido al carrito")
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            enabled = selectedSize.value != null && product.stock > 0,
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Text(
                                text = if (product.stock > 0) "Agregar al carrito" else "Agotado",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Text("Reseñas", style = MaterialTheme.typography.headlineSmall)
                    }
                }
            }
            items(product.reviews) { review ->
                ListItem(
                    headlineContent = { Text(review.author, fontWeight = FontWeight.Bold) },
                    supportingContent = { Text(review.comment, style = MaterialTheme.typography.bodyMedium) },
                    trailingContent = {
                        Row {
                            for (i in 1..5) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = if (i <= review.rating) Color.Yellow else Color.Gray,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                )
                HorizontalDivider()
            }
        }
    }
}
