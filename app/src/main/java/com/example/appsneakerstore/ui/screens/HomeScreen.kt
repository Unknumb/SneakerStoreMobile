package com.example.appsneakerstore.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appsneakerstore.R
import com.example.appsneakerstore.viewmodel.ProductViewModel

@Composable
fun HomeScreen(
    viewModel: ProductViewModel = viewModel(),
    onProductClick: (Int) -> Unit
) {
    val productList = viewModel.products.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(productList.value.size) { index ->
            val product = productList.value[index]
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { onProductClick(product.id) },
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(modifier = Modifier.padding(16.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.sneaker_placeholder),
                        contentDescription = product.name,
                        modifier = Modifier
                            .size(80.dp)
                            .padding(end = 16.dp),
                        contentScale = ContentScale.Crop
                    )
                    Column {
                        Text(text = product.name, style = MaterialTheme.typography.titleMedium)
                        Text(text = "$${product.price}", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}
