package com.example.appsneakerstore.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.appsneakerstore.ui.screens.CartScreen
import com.example.appsneakerstore.ui.screens.HomeScreen
import com.example.appsneakerstore.ui.screens.ProductDetailScreen
import com.example.appsneakerstore.viewmodel.ProductViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val viewModel: ProductViewModel = viewModel()

    // Observa los productos como estado Compose
    val productListState = viewModel.products.collectAsState()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                viewModel = viewModel,
                onProductClick = { productId ->
                    navController.navigate("detail/$productId")
                }
            )
        }
        composable(
            route = "detail/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId") ?: 0
            val product = productListState.value.find { it.id == productId }

            product?.let {
                ProductDetailScreen(
                    product = it,
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }
        composable("cart") {
            CartScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
