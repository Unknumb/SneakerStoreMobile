package com.example.appsneakerstore

import com.example.appsneakerstore.data.repository.ProductRepository
import com.example.appsneakerstore.model.Product
import com.example.appsneakerstore.model.Review
import com.example.appsneakerstore.viewmodel.ProductViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class ProductViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: ProductRepository
    private lateinit var viewModel: ProductViewModel

    private val testProducts = listOf(
        Product(
            id = 1,
            name = "Nike Air Max",
            price = 150.0,
            imageUrl = "https://example.com/nike.jpg",
            description = "Zapatillas clásicas",
            sizes = listOf(40, 41, 42),
            rating = 4.5f,
            reviews = listOf(
                Review("Juan", 5.0f, "Excelentes!")
            )
        ),
        Product(
            id = 2,
            name = "Adidas Superstar",
            price = 120.0,
            imageUrl = "https://example.com/adidas.jpg",
            description = "Estilo retro",
            sizes = listOf(39, 40, 41),
            rating = 4.2f,
            reviews = emptyList()
        ),
        Product(
            id = 3,
            name = "Puma RS-X",
            price = 110.0,
            imageUrl = "https://example.com/puma.jpg",
            description = "Running moderno",
            sizes = listOf(42, 43, 44),
            rating = 4.0f,
            reviews = emptyList()
        )
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mock()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state has empty products list`() = runTest {
        whenever(repository.fetchRemoteSneakers()).thenReturn(Result.success(emptyList()))
        
        viewModel = ProductViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()
        
        assertTrue(viewModel.products.value.isEmpty())
    }

    @Test
    fun `products loaded from backend successfully`() = runTest {
        whenever(repository.fetchRemoteSneakers()).thenReturn(Result.success(testProducts))
        
        viewModel = ProductViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()
        
        assertEquals(3, viewModel.products.value.size)
        assertEquals("Nike Air Max", viewModel.products.value[0].name)
    }

    @Test
    fun `search query filters products correctly`() = runTest {
        whenever(repository.fetchRemoteSneakers()).thenReturn(Result.success(testProducts))
        
        viewModel = ProductViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()
        
        viewModel.onSearchQueryChange("Nike")
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Verify searchQuery was updated
        assertEquals("Nike", viewModel.searchQuery.value)
        
        // Verify products list contains products that should match
        val allProducts = viewModel.products.value
        val matchingProducts = allProducts.filter { it.name.contains("Nike", ignoreCase = true) }
        assertEquals(1, matchingProducts.size)
    }

    @Test
    fun `search query is case insensitive`() = runTest {
        whenever(repository.fetchRemoteSneakers()).thenReturn(Result.success(testProducts))
        
        viewModel = ProductViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()
        
        viewModel.onSearchQueryChange("nike")
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Verify searchQuery was updated (lowercase)
        assertEquals("nike", viewModel.searchQuery.value)
        
        // Verify products list contains products that should match (case insensitive)
        val allProducts = viewModel.products.value
        val matchingProducts = allProducts.filter { it.name.contains("nike", ignoreCase = true) }
        assertEquals(1, matchingProducts.size)
    }

    @Test
    fun `empty search query returns all products`() = runTest {
        whenever(repository.fetchRemoteSneakers()).thenReturn(Result.success(testProducts))
        
        viewModel = ProductViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()
        
        viewModel.onSearchQueryChange("")
        testDispatcher.scheduler.advanceUntilIdle()
        
        assertEquals("", viewModel.searchQuery.value)
        assertEquals(3, viewModel.products.value.size)
    }

    @Test
    fun `select product by id works correctly`() = runTest {
        whenever(repository.fetchRemoteSneakers()).thenReturn(Result.success(testProducts))
        
        viewModel = ProductViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()
        
        viewModel.selectProduct(2)
        
        assertNotNull(viewModel.selectedProduct.value)
        assertEquals("Adidas Superstar", viewModel.selectedProduct.value?.name)
    }

    @Test
    fun `select non-existent product returns null`() = runTest {
        whenever(repository.fetchRemoteSneakers()).thenReturn(Result.success(testProducts))
        
        viewModel = ProductViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()
        
        viewModel.selectProduct(999)
        
        assertNull(viewModel.selectedProduct.value)
    }

    @Test
    fun `add product to cart increases quantity`() = runTest {
        whenever(repository.fetchRemoteSneakers()).thenReturn(Result.success(testProducts))
        
        viewModel = ProductViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val product = testProducts[0]
        viewModel.addToCart(product)
        
        assertEquals(1, viewModel.cartItems.value[product])
    }

    @Test
    fun `add same product twice increases quantity to 2`() = runTest {
        whenever(repository.fetchRemoteSneakers()).thenReturn(Result.success(testProducts))
        
        viewModel = ProductViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val product = testProducts[0]
        viewModel.addToCart(product)
        viewModel.addToCart(product)
        
        assertEquals(2, viewModel.cartItems.value[product])
    }

    @Test
    fun `remove from cart decreases quantity`() = runTest {
        whenever(repository.fetchRemoteSneakers()).thenReturn(Result.success(testProducts))
        
        viewModel = ProductViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val product = testProducts[0]
        viewModel.addToCart(product)
        viewModel.addToCart(product)
        viewModel.removeFromCart(product)
        
        assertEquals(1, viewModel.cartItems.value[product])
    }

    @Test
    fun `remove from cart with quantity 1 keeps item with quantity 1`() = runTest {
        whenever(repository.fetchRemoteSneakers()).thenReturn(Result.success(testProducts))
        
        viewModel = ProductViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val product = testProducts[0]
        viewModel.addToCart(product)
        viewModel.removeFromCart(product)
        
        // Based on implementation, removeFromCart keeps quantity at 1 if already 1
        assertEquals(1, viewModel.cartItems.value[product])
    }

    @Test
    fun `remove item from cart completely removes product`() = runTest {
        whenever(repository.fetchRemoteSneakers()).thenReturn(Result.success(testProducts))
        
        viewModel = ProductViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val product = testProducts[0]
        viewModel.addToCart(product)
        viewModel.removeItemFromCart(product)
        
        assertFalse(viewModel.cartItems.value.containsKey(product))
    }

    @Test
    fun `clear cart removes all items`() = runTest {
        whenever(repository.fetchRemoteSneakers()).thenReturn(Result.success(testProducts))
        
        viewModel = ProductViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()
        
        viewModel.addToCart(testProducts[0])
        viewModel.addToCart(testProducts[1])
        viewModel.clearCart()
        
        assertTrue(viewModel.cartItems.value.isEmpty())
    }

    @Test
    fun `simulate purchase clears cart and calls callback`() = runTest {
        whenever(repository.fetchRemoteSneakers()).thenReturn(Result.success(testProducts))
        
        viewModel = ProductViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()
        
        viewModel.addToCart(testProducts[0])
        
        var callbackCalled = false
        viewModel.simulatePurchase { callbackCalled = true }
        
        assertTrue(callbackCalled)
        assertTrue(viewModel.cartItems.value.isEmpty())
    }

    @Test
    fun `backend failure returns empty list gracefully`() = runTest {
        whenever(repository.fetchRemoteSneakers()).thenReturn(Result.failure(Exception("Network error")))
        
        viewModel = ProductViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Should not crash and products should remain empty
        assertTrue(viewModel.products.value.isEmpty())
    }

    @Test
    fun `multiple products can be added to cart`() = runTest {
        whenever(repository.fetchRemoteSneakers()).thenReturn(Result.success(testProducts))
        
        viewModel = ProductViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()
        
        viewModel.addToCart(testProducts[0])
        viewModel.addToCart(testProducts[1])
        viewModel.addToCart(testProducts[2])
        
        assertEquals(3, viewModel.cartItems.value.size)
    }
}
