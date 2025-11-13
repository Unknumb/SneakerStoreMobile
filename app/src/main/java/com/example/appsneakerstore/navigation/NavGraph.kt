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
    val viewModel: ProductViewModel = viewModel()
    val userViewModel: UserViewModel = viewModel()

    val productListState = viewModel.products.collectAsState()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                onLogin = {
                    userViewModel.login("Test User")
                    navController.navigate("home")
                },
                onGuestLogin = { navController.navigate("home") },
                onRegisterClick = { navController.navigate("register") }
            )
        }
        composable("register") {
            RegisterScreen(onRegister = { navController.navigate("home") })
        }
        composable("home") {
            HomeScreen(
                viewModel = viewModel,
                userViewModel = userViewModel,
                onProductClick = { productId ->
                    navController.navigate("detail/$productId")
                },
                onCartClick = { navController.navigate("cart") },
                onProfileClick = { navController.navigate("login") }
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
