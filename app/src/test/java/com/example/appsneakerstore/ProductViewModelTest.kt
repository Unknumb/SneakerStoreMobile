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
        ),
        Product(
            id = 3,
            name = "Puma RS-X",
            price = 110.0,
            imageUrl = "https://example.com/puma.jpg",
            description = "Running moderno",
            sizes = listOf(42, 43, 44),
            rating = 4.0f,
            reviews = emptyList(),
            stock = 20,
            color = "White"
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

    // Verifica que el estado inicial de la lista de productos esté vacío antes de cargar datos
    @Test
    fun `initial state has empty products list`() = runTest {
        whenever(repository.fetchRemoteSneakers()).thenReturn(Result.success(emptyList()))
        
        viewModel = ProductViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()
        
        assertTrue(viewModel.products.value.isEmpty())
    }

    // Verifica que los productos se carguen correctamente desde el repositorio (backend simulado)
    @Test
    fun `products loaded from backend successfully`() = runTest {
        whenever(repository.fetchRemoteSneakers()).thenReturn(Result.success(testProducts))
        
        viewModel = ProductViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()
        
        assertEquals(3, viewModel.products.value.size)
        assertEquals("Nike Air Max", viewModel.products.value[0].name)
    }

    // Verifica que el buscador filtre correctamente los productos por nombre
    @Test
    fun `search query filters products correctly`() = runTest {
        whenever(repository.fetchRemoteSneakers()).thenReturn(Result.success(testProducts))
        
        viewModel = ProductViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()
        
        viewModel.onSearchQueryChange("Nike")
        testDispatcher.scheduler.advanceUntilIdle()
        
        assertEquals("Nike", viewModel.searchQuery.value)
        
        val allProducts = viewModel.products.value
        val matchingProducts = allProducts.filter { it.name.contains("Nike", ignoreCase = true) }
        assertEquals(1, matchingProducts.size)
    }

    // Verifica que la selección de un producto por ID funcione y actualice el estado seleccionado
    @Test
    fun `select product by id works correctly`() = runTest {
        whenever(repository.fetchRemoteSneakers()).thenReturn(Result.success(testProducts))
        
        viewModel = ProductViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()
        
        viewModel.selectProduct(2)
        
        assertNotNull(viewModel.selectedProduct.value)
        assertEquals("Adidas Superstar", viewModel.selectedProduct.value?.name)
    }

    // Verifica que al agregar un producto al carrito, la cantidad se incremente correctamente
    @Test
    fun `add product to cart increases quantity`() = runTest {
        whenever(repository.fetchRemoteSneakers()).thenReturn(Result.success(testProducts))
        
        viewModel = ProductViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val product = testProducts[0]
        viewModel.addToCart(product)
        
        assertEquals(1, viewModel.cartItems.value[product])
    }

    // Verifica que no se pueda agregar más cantidad de un producto que su stock disponible
    @Test
    fun `add product to cart respects stock limit`() = runTest {
        whenever(repository.fetchRemoteSneakers()).thenReturn(Result.success(testProducts))
        
        viewModel = ProductViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val product = testProducts[1] // Stock es 5
        
        // Agregar 5 veces (hasta el límite)
        repeat(5) { viewModel.addToCart(product) }
        assertEquals(5, viewModel.cartItems.value[product])
        
        // Intentar agregar una sexta vez
        viewModel.addToCart(product)
        // Debería mantenerse en 5
        assertEquals(5, viewModel.cartItems.value[product])
    }

    // Verifica que al reducir la cantidad de un producto en el carrito, esta disminuya correctamente
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

    // Verifica que la función clearCart elimine todos los elementos del carrito
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
}
