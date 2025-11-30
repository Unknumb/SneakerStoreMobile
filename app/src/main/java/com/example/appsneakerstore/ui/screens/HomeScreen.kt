package com.example.appsneakerstore.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.appsneakerstore.ui.components.AppBottomBar
import com.example.appsneakerstore.viewmodel.ProductViewModel
import com.example.appsneakerstore.viewmodel.ProductViewModelFactory
import com.example.appsneakerstore.viewmodel.UserViewModel
import kotlinx.coroutines.delay
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: ProductViewModel = viewModel(factory = ProductViewModelFactory()),
    userViewModel: UserViewModel = viewModel(),
    onProductClick: (Int) -> Unit,
    onCartClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    onProfileClick: () -> Unit,
    onLoginClick: () -> Unit = {},
    onSearchClick: () -> Unit = {}
) {
    val productList by viewModel.filteredProducts.collectAsState()
    val clpFormat = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CL"))
    val favorites by userViewModel.favorites.collectAsState()
    val currentUser by userViewModel.username.collectAsState()
    val hasShownLoginPopup by userViewModel.hasShownLoginPopup.collectAsState()

    // Mostrar login después de 3 segundos si no hay usuario logueado y es la primera vez
    LaunchedEffect(Unit) {
        if (!hasShownLoginPopup && currentUser == null) {
            delay(3000)
            if (currentUser == null) {
                userViewModel.markLoginPopupShown()
                onLoginClick()
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Sneaker Store", style = MaterialTheme.typography.titleLarge) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        },
        bottomBar = {
            AppBottomBar(
                currentRoute = "home",
                onHomeClick = { /* Ya estamos en home */ },
                onSearchClick = onSearchClick,
                onCartClick = onCartClick,
                onProfileClick = onProfileClick,
                productViewModel = viewModel
            )
        }
    ) { paddingValues ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(productList) { product ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.selectProduct(product.id)
                                onProductClick(product.id)
                            },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Box {
                            Column {
                                AsyncImage(
                                    model = product.imageUrl,
                                    contentDescription = product.name,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(1f),
                                    contentScale = ContentScale.Fit
                                )
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = product.name,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = clpFormat.format(product.price),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                            if (favorites.contains(product.id)) {
                                Icon(
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = "Favorite",
                                    tint = Color.Red,
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(8.dp)
                                )
                            }
                        }
                    }
                }
    }
}
}