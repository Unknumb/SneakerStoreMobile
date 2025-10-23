package com.example.appsneakerstore.viewmodel

import androidx.lifecycle.ViewModel
import com.example.appsneakerstore.data.local.MockData
import com.example.appsneakerstore.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProductViewModel : ViewModel() {
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    init {
        loadProducts()
    }

    private fun loadProducts() {
        _products.value = MockData.products
    }
}
