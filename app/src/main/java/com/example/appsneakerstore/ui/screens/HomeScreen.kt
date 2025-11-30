package com.example.appsneakerstore.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.appsneakerstore.R
import com.example.appsneakerstore.ui.components.AppBottomBar
import com.example.appsneakerstore.viewmodel.ProductViewModel
import com.example.appsneakerstore.viewmodel.UserViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: ProductViewModel,
    userViewModel: UserViewModel,
    onProductClick: (Int) -> Unit,
    onCartClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    onProfileClick: () -> Unit,
    onLoginClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {}
) {
    val productList by viewModel.filteredProducts.collectAsState()
    val clpFormat = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CL"))
    val favorites by userViewModel.favorites.collectAsState()
    val currentUser by userViewModel.username.collectAsState()
    val hasShownLoginPopup by userViewModel.hasShownLoginPopup.collectAsState()
    
    // Estado para el BottomSheet
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val showLoginSheet = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // Marcar como mostrado si el usuario navega a otra pantalla (usando DisposableEffect)
    androidx.compose.runtime.DisposableEffect(Unit) {
        onDispose {
            // Si el usuario sale de Home antes de los 3 segundos, marcar como mostrado
            if (!hasShownLoginPopup) {
                userViewModel.markLoginPopupShown()
            }
        }
    }

    // Mostrar login después de 3 segundos si no hay usuario logueado y es la primera vez
    LaunchedEffect(Unit) {
        if (!hasShownLoginPopup && currentUser == null) {
            delay(3000)
            if (currentUser == null && !hasShownLoginPopup) {
                userViewModel.markLoginPopupShown()
                showLoginSheet.value = true
            }
        }
    }

    // Cerrar el sheet si el usuario inicia sesión
    LaunchedEffect(currentUser) {
        if (currentUser != null && showLoginSheet.value) {
            scope.launch {
                sheetState.hide()
                showLoginSheet.value = false
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        androidx.compose.foundation.Image(
                            painter = painterResource(id = R.drawable.logosn),
                            contentDescription = "Logo",
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Sneaker Store", style = MaterialTheme.typography.titleLarge)
                    }
                },
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
                                        .aspectRatio(1f)
                                        .background(Color.LightGray.copy(alpha = 0.3f)),
                                    contentScale = ContentScale.Fit,
                                    error = painterResource(id = android.R.drawable.ic_menu_gallery),
                                    placeholder = painterResource(id = android.R.drawable.ic_menu_gallery)
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

    // Modal Bottom Sheet para Login
    if (showLoginSheet.value) {
        ModalBottomSheet(
            onDismissRequest = {
                showLoginSheet.value = false
            },
            sheetState = sheetState,
            containerColor = Color.White
        ) {
            LoginBottomSheetContent(
                userViewModel = userViewModel,
                onClose = {
                    scope.launch {
                        sheetState.hide()
                        showLoginSheet.value = false
                    }
                },
                onRegisterClick = {
                    scope.launch {
                        sheetState.hide()
                        showLoginSheet.value = false
                        onRegisterClick()
                    }
                }
            )
        }
    }
}

@Composable
private fun LoginBottomSheetContent(
    userViewModel: UserViewModel,
    onClose: () -> Unit,
    onRegisterClick: () -> Unit
) {
    val errorState by userViewModel.loginError.collectAsState()
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    
    // Obtener la altura de la pantalla y usar 3/4
    val configuration = androidx.compose.ui.platform.LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val sheetHeight = screenHeight * 0.70f

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(sheetHeight)
            .padding(horizontal = 32.dp)
            .padding(bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header con botón de cerrar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                androidx.compose.foundation.Image(
                    painter = painterResource(id = R.drawable.logosn),
                    contentDescription = "Logo",
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Sneaker Store", style = MaterialTheme.typography.titleMedium)
            }
            IconButton(onClick = onClose) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cerrar"
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text("BIENVENIDO", style = MaterialTheme.typography.headlineLarge)
        
        Spacer(modifier = Modifier.height(32.dp))
        
        OutlinedTextField(
            value = username.value,
            onValueChange = { username.value = it },
            label = { Text("Usuario") },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            isError = errorState != null,
            shape = MaterialTheme.shapes.medium
        )
        
        errorState?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = { userViewModel.login(username.value, password.value) },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("INICIAR SESIÓN", style = MaterialTheme.typography.labelLarge)
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedButton(
            onClick = onRegisterClick,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("CREAR CUENTA", style = MaterialTheme.typography.labelLarge)
        }
    }
}