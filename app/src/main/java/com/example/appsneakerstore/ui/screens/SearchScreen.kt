package com.example.appsneakerstore.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
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
    
    // Estados para filtros y ordenamiento
    val showBrandFilter = remember { mutableStateOf(false) }
    val showSortOptions = remember { mutableStateOf(false) }
    val selectedBrands = remember { mutableStateOf(setOf<String>()) }
    val sortBy = remember { mutableStateOf("none") } // "none", "brand", "price"
    val isAscending = remember { mutableStateOf(true) }
    
    // Obtener marcas únicas
    val allBrands = remember(productList) {
        productList.map { it.name.split(" ").firstOrNull() ?: "" }
            .filter { it.isNotBlank() }
            .distinct()
            .sorted()
    }
    
    // Filtrar y ordenar productos
    val filteredAndSortedProducts = remember(productList, selectedBrands.value, sortBy.value, isAscending.value) {
        var result = if (selectedBrands.value.isEmpty()) {
            productList
        } else {
            productList.filter { product ->
                val brand = product.name.split(" ").firstOrNull() ?: ""
                selectedBrands.value.contains(brand)
            }
        }
        
        result = when (sortBy.value) {
            "brand" -> if (isAscending.value) result.sortedBy { it.name } else result.sortedByDescending { it.name }
            "price" -> if (isAscending.value) result.sortedBy { it.price } else result.sortedByDescending { it.price }
            else -> result
        }
        
        result
    }

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
                
                // Campo de búsqueda con estilo OutlinedTextField
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { productViewModel.onSearchQueryChange(it) },
                    placeholder = { Text("Buscar zapatillas...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = MaterialTheme.shapes.medium,
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedBorderColor = MaterialTheme.colorScheme.primary
                    )
                )
                
                // Fila de filtros
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Botón de marcas
                    Box(modifier = Modifier.weight(1f)) {
                        OutlinedButton(
                            onClick = { showBrandFilter.value = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Text(
                                if (selectedBrands.value.isEmpty()) "Todas las marcas" 
                                else "${selectedBrands.value.size} marcas",
                                maxLines = 1
                            )
                        }
                        DropdownMenu(
                            expanded = showBrandFilter.value,
                            onDismissRequest = { showBrandFilter.value = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Limpiar filtros") },
                                onClick = {
                                    selectedBrands.value = emptySet()
                                    showBrandFilter.value = false
                                }
                            )
                            HorizontalDivider()
                            allBrands.forEach { brand ->
                                DropdownMenuItem(
                                    text = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Checkbox(
                                                checked = selectedBrands.value.contains(brand),
                                                onCheckedChange = null
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(brand)
                                        }
                                    },
                                    onClick = {
                                        selectedBrands.value = if (selectedBrands.value.contains(brand)) {
                                            selectedBrands.value - brand
                                        } else {
                                            selectedBrands.value + brand
                                        }
                                    }
                                )
                            }
                        }
                    }
                    
                    // Botón de ordenar
                    Box(modifier = Modifier.weight(1f)) {
                        OutlinedButton(
                            onClick = { showSortOptions.value = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Text(
                                when (sortBy.value) {
                                    "brand" -> "Por marca"
                                    "price" -> "Por precio"
                                    else -> "Ordenar por"
                                },
                                maxLines = 1
                            )
                        }
                        DropdownMenu(
                            expanded = showSortOptions.value,
                            onDismissRequest = { showSortOptions.value = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Sin ordenar") },
                                onClick = {
                                    sortBy.value = "none"
                                    showSortOptions.value = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Por marca") },
                                onClick = {
                                    sortBy.value = "brand"
                                    showSortOptions.value = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Por precio") },
                                onClick = {
                                    sortBy.value = "price"
                                    showSortOptions.value = false
                                }
                            )
                        }
                    }
                    
                    // Botón de dirección (asc/desc)
                    FilledTonalIconButton(
                        onClick = { isAscending.value = !isAscending.value },
                        enabled = sortBy.value != "none"
                    ) {
                        Icon(
                            imageVector = if (isAscending.value) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                            contentDescription = if (isAscending.value) "Ascendente" else "Descendente"
                        )
                    }
                }
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
            items(filteredAndSortedProducts) { product ->
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
                        // Botón de favorito (siempre visible, tocable)
                        IconButton(
                            onClick = { userViewModel.toggleFavorite(product.id) },
                            modifier = Modifier.align(Alignment.TopEnd)
                        ) {
                            Icon(
                                imageVector = if (favorites.contains(product.id)) 
                                    Icons.Default.Favorite 
                                else 
                                    Icons.Default.FavoriteBorder,
                                contentDescription = "Favorito",
                                tint = if (favorites.contains(product.id)) Color.Red else Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}
