package com.example.appsneakerstore.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.appsneakerstore.ui.components.AppBottomBar
import com.example.appsneakerstore.viewmodel.ProductViewModel
import com.example.appsneakerstore.viewmodel.UserViewModel
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    productViewModel: ProductViewModel,
    userViewModel: UserViewModel,
    onProductClick: (Int) -> Unit,
    onHomeClick: () -> Unit,
    onCartClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val productList by productViewModel.filteredProducts.collectAsState()
    val searchQuery by productViewModel.searchQuery.collectAsState()
    val favorites by userViewModel.favorites.collectAsState()
    val currentUser by userViewModel.username.collectAsState()
    val clpFormat = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CL"))
    
    val focusManager = LocalFocusManager.current
    val gridState = rememberLazyGridState()

    // Ocultar teclado al hacer scroll
    LaunchedEffect(gridState) {
        snapshotFlow { gridState.isScrollInProgress }
            .collect { isScrolling ->
                if (isScrolling) {
                    focusManager.clearFocus()
                }
            }
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier.pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                }
            ) {
                CenterAlignedTopAppBar(
                    title = { Text("Buscar", style = MaterialTheme.typography.titleLarge) },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.White,
                        titleContentColor = Color.Black
                    )
                )
                TextField(
                    value = searchQuery,
                    onValueChange = { productViewModel.onSearchQueryChange(it) },
                    placeholder = { Text("Buscar zapatillas...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.LightGray.copy(alpha = 0.3f),
                        unfocusedContainerColor = Color.LightGray.copy(alpha = 0.3f)
                    ),
                    shape = MaterialTheme.shapes.medium,
                    singleLine = true
                )
            }
        },
        bottomBar = {
            AppBottomBar(
                currentRoute = "search",
                onHomeClick = onHomeClick,
                onSearchClick = { /* Ya estamos en search */ },
                onCartClick = onCartClick,
                onProfileClick = onProfileClick,
                productViewModel = productViewModel
            )
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            state = gridState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 8.dp)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                },
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(productList) { product ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            productViewModel.selectProduct(product.id)
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
