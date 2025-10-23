package com.example.appsneakerstore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.appsneakerstore.ui.screens.HomeScreen
import com.example.appsneakerstore.ui.screens.ProductDetailScreen
import com.example.appsneakerstore.viewmodel.ProductViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    val productViewModel: ProductViewModel = viewModel()

                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") {
                            HomeScreen(
                                viewModel = productViewModel,
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
                            val product = productViewModel.products.value.find { it.id == productId }
                            if (product != null) {
                                ProductDetailScreen(
                                    product = product,
                                    onBack = { navController.popBackStack() }
                                )
                            } else {
                                // Producto no encontrado, volver a la pantalla anterior
                                navController.popBackStack()
                            }
                        }
                    }
                }
            }
        }
    }
}
