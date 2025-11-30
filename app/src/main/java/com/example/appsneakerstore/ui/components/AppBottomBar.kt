package com.example.appsneakerstore.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.example.appsneakerstore.viewmodel.ProductViewModel

@Composable
fun AppBottomBar(
    currentRoute: String,
    onHomeClick: () -> Unit,
    onSearchClick: () -> Unit,
    onCartClick: () -> Unit,
    onProfileClick: () -> Unit,
    productViewModel: ProductViewModel
) {
    val cartItems by productViewModel.cartItems.collectAsState()
    val cartQuantity = cartItems.values.sum()

    NavigationBar(
        containerColor = Color.White,
        contentColor = Color.Black
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Inicio") },
            label = { Text("Inicio") },
            selected = currentRoute == "home",
            onClick = onHomeClick,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Black,
                selectedTextColor = Color.Black,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.LightGray
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Search, contentDescription = "Buscar") },
            label = { Text("Buscar") },
            selected = currentRoute == "search",
            onClick = onSearchClick,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Black,
                selectedTextColor = Color.Black,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.LightGray
            )
        )
        NavigationBarItem(
            icon = {
                BadgedBox(badge = { 
                    if (cartQuantity > 0) Badge { Text(cartQuantity.toString()) } 
                }) {
                    Icon(Icons.Filled.ShoppingCart, contentDescription = "Carrito")
                }
            },
            label = { Text("Carrito") },
            selected = currentRoute == "cart",
            onClick = onCartClick,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Black,
                selectedTextColor = Color.Black,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.LightGray
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Person, contentDescription = "Perfil") },
            label = { Text("Cuenta") },
            selected = currentRoute == "login" || currentRoute == "profile",
            onClick = onProfileClick,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Black,
                selectedTextColor = Color.Black,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.LightGray
            )
        )
    }
}
