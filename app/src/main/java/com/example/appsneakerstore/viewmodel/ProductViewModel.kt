package com.example.appsneakerstore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsneakerstore.data.repository.ProductRepository
import com.example.appsneakerstore.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductViewModel(
    private val repository: ProductRepository
) : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    private val _searchQuery = MutableStateFlow("")

    val products: StateFlow<List<Product>> = _products.asStateFlow()
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val filteredProducts: StateFlow<List<Product>> = _products
        .combine(_searchQuery) { products, query ->
            if (query.isBlank()) {
                products
            } else {
                products.filter { it.name.contains(query, ignoreCase = true) }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )

    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct: StateFlow<Product?> = _selectedProduct.asStateFlow()

    private val _cartItems = MutableStateFlow<Map<Product, Int>>(emptyMap())
    val cartItems: StateFlow<Map<Product, Int>> = _cartItems.asStateFlow()

    init {
        // 1) Cargar productos locales
        loadProducts()

        // 2) Backend desactivado temporalmente (las imágenes del backend están vacías)
        // refreshFromBackend()
    }

    private fun loadProducts() {
        _products.value = repository.getLocalProducts()
    }

    fun refreshFromBackend() {
        viewModelScope.launch {
            val result = repository.fetchRemoteSneakers()
            result.onSuccess { remoteList ->
                if (remoteList.isNotEmpty()) {
                    _products.value = remoteList
                }
            }
            // onFailure: podrías mostrar un snackbar / log si quieres
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun selectProduct(productId: Int) {
        val product = _products.value.find { it.id == productId }
        _selectedProduct.value = product
    }

    fun addToCart(product: Product) {
        val currentMap = _cartItems.value.toMutableMap()
        currentMap[product] = (currentMap[product] ?: 0) + 1
        _cartItems.value = currentMap
    }

    fun removeFromCart(product: Product) {
        val currentMap = _cartItems.value.toMutableMap()
        val currentCount = currentMap[product] ?: 0
        if (currentCount > 1) {
            currentMap[product] = currentCount - 1
        }
        _cartItems.value = currentMap
    }

    fun removeItemFromCart(product: Product) {
        val currentMap = _cartItems.value.toMutableMap()
        currentMap.remove(product)
        _cartItems.value = currentMap
    }

    fun clearCart() {
        _cartItems.value = emptyMap()
    }

    fun simulatePurchase(onPurchaseComplete: () -> Unit) {
        clearCart()
        onPurchaseComplete()
    }
}