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
            ),
            stock = 10,
            color = "Blue"
        ),
        Product(
            id = 2,
            name = "Adidas Superstar",
            price = 120.0,
            imageUrl = "https://example.com/adidas.jpg",
            description = "Estilo retro",
            sizes = listOf(39, 40, 41),
            rating = 4.2f,
            reviews = emptyList(),
            stock = 5,
            color = "Black"
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
    fun `add product to cart increases quantity`() = runTest {
        whenever(repository.fetchRemoteSneakers()).thenReturn(Result.success(testProducts))
        
        viewModel = ProductViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val product = testProducts[0]
        viewModel.addToCart(product)
        
        assertEquals(1, viewModel.cartItems.value[product])
    }

    @Test
    fun `add product to cart respects stock limit`() = runTest {
        whenever(repository.fetchRemoteSneakers()).thenReturn(Result.success(testProducts))
        
        viewModel = ProductViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val product = testProducts[1] // Stock is 5
        
        // Add 5 times (up to limit)
        repeat(5) { viewModel.addToCart(product) }
        assertEquals(5, viewModel.cartItems.value[product])
        
        // Try to add 6th time
        viewModel.addToCart(product)
        // Should stay at 5
        assertEquals(5, viewModel.cartItems.value[product])
    }
}
