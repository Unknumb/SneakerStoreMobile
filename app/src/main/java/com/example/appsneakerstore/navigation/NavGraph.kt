package com.example.appsneakerstore.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.appsneakerstore.ui.screens.*
import com.example.appsneakerstore.viewmodel.ProductViewModel
import com.example.appsneakerstore.viewmodel.UserViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val productViewModel: ProductViewModel = viewModel()
    val userViewModel: UserViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                userViewModel = userViewModel,
                onLogin = { navController.navigate("home") },
                onGuestLogin = { navController.navigate("home") },
                onRegisterClick = { navController.navigate("register") }
            )
        }
        composable("register") {
            RegisterScreen(
                userViewModel = userViewModel,
                onRegister = { navController.navigate("login") }
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
                onProfileClick = { navController.navigate("profile") }
            )
        }
        composable("profile") {
            ProfileScreen(userViewModel = userViewModel, onLoginRedirect = { navController.navigate("login") })
        }
        composable(
            route = "detail/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId") ?: 0
            val product = productViewModel.products.collectAsState().value.find { it.id == productId }

            product?.let {
                ProductDetailScreen(
                    product = it,
                    viewModel = productViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }
        composable("cart") {
            CartScreen(
                viewModel = productViewModel,
                userViewModel = userViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
