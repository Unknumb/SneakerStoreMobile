package com.example.appsneakerstore

import com.example.appsneakerstore.data.local.MockData
import com.example.appsneakerstore.data.remote.SneakerApiService
import com.example.appsneakerstore.data.remote.SneakerDto
import com.example.appsneakerstore.data.repository.ProductRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class ProductRepositoryTest {

    private lateinit var apiService: SneakerApiService
    private lateinit var repository: ProductRepository

    private val mockSneakerDtos = listOf(
        SneakerDto(
            id = 1L,
            marca = "Nike",
            modelo = "Air Max",
            talla = 42.0,
            tallas = listOf(40.0, 41.0, 42.0),
            color = "Blanco",
            precio = 150.0,
            stock = 10,
            image = "https://example.com/nike.jpg"
        ),
        SneakerDto(
            id = 2L,
            marca = "Adidas",
            modelo = "Superstar",
            talla = 41.0,
            tallas = listOf(39.0, 40.0, 41.0),
            color = "Negro",
            precio = 120.0,
            stock = 5,
            image = "https://example.com/adidas.jpg"
        )
    )

    @Before
    fun setup() {
        apiService = mock()
        repository = ProductRepository(apiService)
    }

    // Verifica que getLocalProducts devuelva la lista de productos simulados (MockData)
    @Test
    fun `getLocalProducts returns MockData products`() {
        val products = repository.getLocalProducts()
        
        assertEquals(MockData.products, products)
        assertTrue(products.isNotEmpty())
    }

    // Verifica que fetchRemoteSneakers obtenga los datos del API y los mapee correctamente a objetos Product
    @Test
    fun `fetchRemoteSneakers success returns mapped products`() = runTest {
        whenever(apiService.getSneakers()).thenReturn(mockSneakerDtos)
        
        val result = repository.fetchRemoteSneakers()
        
        assertTrue(result.isSuccess)
        val products = result.getOrNull()
        assertNotNull(products)
        assertEquals(2, products?.size)
        assertEquals("Nike Air Max", products?.get(0)?.name)
        assertEquals(150.0, products?.get(0)?.price ?: 0.0, 0.01)
        assertEquals(10, products?.get(0)?.stock)
        assertEquals("Blanco", products?.get(0)?.color)
    }

    // Verifica que si la API falla, el repositorio devuelva un resultado de fallo controlado
    @Test
    fun `fetchRemoteSneakers failure returns Result failure`() = runTest {
        whenever(apiService.getSneakers()).thenThrow(RuntimeException("Network error"))
        
        val result = repository.fetchRemoteSneakers()
        
        assertTrue(result.isFailure)
    }

    // Verifica que si la API devuelve una lista vacía, el repositorio devuelva una lista vacía de productos
    @Test
    fun `fetchRemoteSneakers empty list returns empty products`() = runTest {
        whenever(apiService.getSneakers()).thenReturn(emptyList())
        
        val result = repository.fetchRemoteSneakers()
        
        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull()?.isEmpty() == true)
    }
}
