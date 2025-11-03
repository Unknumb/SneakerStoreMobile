package com.example.appsneakerstore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.appsneakerstore.ui.components.AppWithSideDrawer
import com.example.appsneakerstore.ui.components.AppSneakerTopBar
import com.example.appsneakerstore.ui.screens.CartScreen
import com.example.appsneakerstore.ui.screens.HomeScreen
import com.example.appsneakerstore.ui.screens.LoginScreen
import com.example.appsneakerstore.ui.screens.ProductDetailScreen
import com.example.appsneakerstore.ui.screens.RegisterScreen
import com.example.appsneakerstore.viewmodel.AuthViewModel
import com.example.appsneakerstore.viewmodel.ProductViewModel
import com.example.appsneakerstore.ui.theme.AppSneakerStoreTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppSneakerStoreTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.fillMaxSize()
                ) {
                    val navController = rememberNavController()
                    val scope = rememberCoroutineScope()
                    val drawerState = rememberDrawerState(DrawerValue.Closed)
                    val productViewModel: ProductViewModel = viewModel()
                    val authViewModel: AuthViewModel = viewModel()
                    val authState = authViewModel.uiState.collectAsState()

                    // Determine start destination based on auth state
                    val startDestination = if (authState.value.isLoggedIn) "home" else "login"

                    NavHost(
                        navController = navController,
                        startDestination = startDestination
                    ) {
                        // Authentication screens
                        composable("login") {
                            LoginScreen(
                                viewModel = authViewModel,
                                onLoginSuccess = {
                                    navController.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                },
                                onNavigateToRegister = {
                                    navController.navigate("register")
                                }
                            )
                        }

                        composable("register") {
                            RegisterScreen(
                                viewModel = authViewModel,
                                onRegisterSuccess = {
                                    navController.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                },
                                onNavigateBack = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        // Main app screens (requires auth)
                        composable("home") {
                            AppWithSideDrawer(
                                navController = navController,
                                drawerState = drawerState,
                                authViewModel = authViewModel,
                                onLogout = {
                                    authViewModel.logout()
                                    navController.navigate("login") {
                                        popUpTo(0) { inclusive = true }
                                    }
                                }
                            ) {
                                Scaffold(
                                    topBar = {
                                        AppSneakerTopBar(
                                            openDrawer = {
                                                scope.launch {
                                                    drawerState.open()
                                                }
                                            },
                                            onSearchClick = { /* Acción buscar */ },
                                            onCartClick = {
                                                navController.navigate("cart")
                                            },
                                            onProfileClick = { /* Acción perfil */ }
                                        )
                                    },
                                    modifier = Modifier.fillMaxSize()
                                ) { paddingValues ->
                                    Box(modifier = Modifier.padding(paddingValues)) {
                                        HomeScreen(
                                            viewModel = productViewModel,
                                            onProductClick = { productId ->
                                                navController.navigate("detail/$productId")
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        composable(
                            route = "detail/{productId}",
                            arguments = listOf(navArgument("productId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val productId = backStackEntry.arguments?.getInt("productId") ?: 0
                            val product =
                                productViewModel.products.collectAsState().value.find { it.id == productId }
                            
                            AppWithSideDrawer(
                                navController = navController,
                                drawerState = drawerState,
                                authViewModel = authViewModel,
                                onLogout = {
                                    authViewModel.logout()
                                    navController.navigate("login") {
                                        popUpTo(0) { inclusive = true }
                                    }
                                }
                            ) {
                                Scaffold(
                                    topBar = {
                                        AppSneakerTopBar(
                                            openDrawer = {
                                                scope.launch {
                                                    drawerState.open()
                                                }
                                            },
                                            onSearchClick = { /* Acción buscar */ },
                                            onCartClick = {
                                                navController.navigate("cart")
                                            },
                                            onProfileClick = { /* Acción perfil */ }
                                        )
                                    },
                                    modifier = Modifier.fillMaxSize()
                                ) { paddingValues ->
                                    Box(modifier = Modifier.padding(paddingValues)) {
                                        if (product != null) {
                                            ProductDetailScreen(
                                                product = product,
                                                viewModel = productViewModel,
                                                onBack = { navController.popBackStack() }
                                            )
                                        } else {
                                            navController.popBackStack()
                                        }
                                    }
                                }
                            }
                        }

                        composable("cart") {
                            AppWithSideDrawer(
                                navController = navController,
                                drawerState = drawerState,
                                authViewModel = authViewModel,
                                onLogout = {
                                    authViewModel.logout()
                                    navController.navigate("login") {
                                        popUpTo(0) { inclusive = true }
                                    }
                                }
                            ) {
                                Scaffold(
                                    topBar = {
                                        AppSneakerTopBar(
                                            openDrawer = {
                                                scope.launch {
                                                    drawerState.open()
                                                }
                                            },
                                            onSearchClick = { /* Acción buscar */ },
                                            onCartClick = {
                                                navController.navigate("cart")
                                            },
                                            onProfileClick = { /* Acción perfil */ }
                                        )
                                    },
                                    modifier = Modifier.fillMaxSize()
                                ) { paddingValues ->
                                    Box(modifier = Modifier.padding(paddingValues)) {
                                        CartScreen(
                                            viewModel = productViewModel,
                                            onBack = { navController.popBackStack() }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
