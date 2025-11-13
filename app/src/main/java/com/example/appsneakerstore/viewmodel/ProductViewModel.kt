package com.example.appsneakerstore.viewmodel

import MockData
import androidx.lifecycle.ViewModel
import com.example.appsneakerstore.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProductViewModel : ViewModel() {
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct: StateFlow<Product?> = _selectedProduct.asStateFlow()

    private val _cartItems = MutableStateFlow<Map<Product, Int>>(emptyMap())
    val cartItems: StateFlow<Map<Product, Int>> = _cartItems.asStateFlow()

    init {
        loadProducts()
    }

    private fun loadProducts() {
        _products.value = MockData.products
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
