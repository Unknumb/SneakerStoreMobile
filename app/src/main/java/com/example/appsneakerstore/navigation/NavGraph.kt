package com.example.appsneakerstore.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appsneakerstore.ui.screens.*
import com.example.appsneakerstore.viewmodel.ProductViewModel
import com.example.appsneakerstore.viewmodel.ProductViewModelFactory
import com.example.appsneakerstore.viewmodel.UserViewModel

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    // ViewModels creados aquí
    val productViewModel: ProductViewModel = viewModel(factory = ProductViewModelFactory())
    val userViewModel: UserViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("login") {
            LoginScreen(
                userViewModel = userViewModel,
                onLogin = { 
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                onRegisterClick = { navController.navigate("register") },
                onClose = { navController.popBackStack() }
            )
        }

        composable("register") {
            RegisterScreen(
                userViewModel = userViewModel,
                onRegister = { navController.navigate("login") },
                onBack = { navController.popBackStack() }
            )
        }

        composable("home") {
            HomeScreen(
                viewModel = productViewModel,
                userViewModel = userViewModel,
                onProductClick = { productId ->
                    navController.navigate("detail/$productId")
                },
                onCartClick = { navController.navigate("cart") },
                onFavoritesClick = { navController.navigate("favorites") },
                onProfileClick = { navController.navigate("profile") },
                onLoginClick = { navController.navigate("login") }
            )
        }

        composable("favorites") {
            FavoritesScreen(
                userViewModel = userViewModel,
                productViewModel = productViewModel,
                onBack = { navController.popBackStack() },
                onProductClick = { productId ->
                    navController.navigate("detail/$productId")
                }
            )
        }

        composable("profile") {
            ProfileScreen(
                userViewModel = userViewModel,
                onLoginRedirect = { navController.navigate("login") },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "detail/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId") ?: 0
            val product = productViewModel.products
                .collectAsState()
                .value
                .find { it.id == productId }

            product?.let {
                ProductDetailScreen(
                    product = it,
                    viewModel = productViewModel,
                    userViewModel = userViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }

        composable("cart") {
            CartScreen(
                viewModel = productViewModel,
                userViewModel = userViewModel,
                onBack = { navController.popBackStack() },
                onCheckout = { navController.navigate("checkout") }
            )
        }

        composable("checkout") {
            CheckoutScreen(
                onBack = { navController.popBackStack() },
                onCheckoutComplete = { navController.navigate("order_success") },
                productViewModel = productViewModel,
                userViewModel = userViewModel
            )
        }

        composable("order_success") {
            OrderSuccessScreen(onBackToHome = { navController.navigate("home") })
        }
    }
}