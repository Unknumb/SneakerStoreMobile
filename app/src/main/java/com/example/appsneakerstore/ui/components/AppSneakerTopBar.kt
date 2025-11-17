package com.example.appsneakerstore.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appsneakerstore.viewmodel.ProductViewModel
import com.example.appsneakerstore.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSneakerTopBar(
    openDrawer: () -> Unit,
    onCartClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    userViewModel: UserViewModel = viewModel(),
    productViewModel: ProductViewModel = viewModel()
) {
    var isSearchVisible by remember { mutableStateOf(false) }
    val searchQuery by productViewModel.searchQuery.collectAsState()
    val username by userViewModel.username.collectAsState()
    val cartItems by productViewModel.cartItems.collectAsState()
    val cartQuantity = cartItems.values.sum()

    TopAppBar(
        navigationIcon = {
            IconButton(onClick = openDrawer) {
                Icon(Icons.Filled.Menu, contentDescription = "Menu")
            }
        },
        title = {
            if (isSearchVisible) {
                TextField(
                    value = searchQuery,
                    onValueChange = { productViewModel.onSearchQueryChange(it) },
                    placeholder = { Text("Buscar...") },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )
            } else {
                Text(
                    text = "Sneaker Store",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        },
        actions = {
            IconButton(onClick = { isSearchVisible = !isSearchVisible }) {
                Icon(Icons.Filled.Search, contentDescription = "Buscar")
            }
            BadgedBox(badge = { if (cartQuantity > 0) Badge { Text(cartQuantity.toString()) } }) {
                IconButton(onClick = onCartClick) {
                    Icon(Icons.Filled.ShoppingCart, contentDescription = "Carrito")
                }
            }
            if (username != null) {
                IconButton(onClick = onFavoritesClick) {
                    Icon(Icons.Filled.Favorite, contentDescription = "Favoritos")
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
            titleContentColor = Color.Black,
            actionIconContentColor = Color.Black,
            navigationIconContentColor = Color.Black
        )
    )
}
