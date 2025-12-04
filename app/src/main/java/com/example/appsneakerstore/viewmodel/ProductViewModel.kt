package com.example.appsneakerstore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsneakerstore.data.remote.RetrofitClient
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
    private val _dolarValue = MutableStateFlow<Double?>(null)
    private val _isLoading = MutableStateFlow(false)

    val products: StateFlow<List<Product>> = _products.asStateFlow()
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    val dolarValue: StateFlow<Double?> = _dolarValue.asStateFlow()
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

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
        refreshProducts()
        fetchDolarValue()
    }

    private fun loadProducts() {
        _products.value = repository.getLocalProducts()
    }

    fun refreshProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.fetchRemoteSneakers()
            result.onSuccess { remoteList ->
                if (remoteList.isNotEmpty()) {
                    _products.value = remoteList
                }
            }
            _isLoading.value = false
        }
    }

    private fun fetchDolarValue() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.mindicatorApi.getIndicators()
                _dolarValue.value = response.dolar.valor
            } catch (e: Exception) {
                // Manejar error o dejar null
                e.printStackTrace()
            }
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
        val currentQuantity = currentMap[product] ?: 0
        
        if (currentQuantity < product.stock) {
            currentMap[product] = currentQuantity + 1
            _cartItems.value = currentMap
        }
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