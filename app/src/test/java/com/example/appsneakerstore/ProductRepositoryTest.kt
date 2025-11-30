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
import java.io.IOException

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

    @Test
    fun `getLocalProducts returns MockData products`() {
        val products = repository.getLocalProducts()
        
        assertEquals(MockData.products, products)
        assertTrue(products.isNotEmpty())
    }

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
    }

    @Test
    fun `fetchRemoteSneakers maps imageUrl correctly`() = runTest {
        whenever(apiService.getSneakers()).thenReturn(mockSneakerDtos)
        
        val result = repository.fetchRemoteSneakers()
        
        val products = result.getOrNull()
        assertEquals("https://example.com/nike.jpg", products?.get(0)?.imageUrl)
    }

    @Test
    fun `fetchRemoteSneakers maps sizes correctly`() = runTest {
        whenever(apiService.getSneakers()).thenReturn(mockSneakerDtos)
        
        val result = repository.fetchRemoteSneakers()
        
        val products = result.getOrNull()
        assertEquals(listOf(40, 41, 42), products?.get(0)?.sizes)
    }

    @Test
    fun `fetchRemoteSneakers failure returns Result failure`() = runTest {
        whenever(apiService.getSneakers()).thenThrow(RuntimeException("Network error"))
        
        val result = repository.fetchRemoteSneakers()
        
        assertTrue(result.isFailure)
    }

    @Test
    fun `fetchRemoteSneakers empty list returns empty products`() = runTest {
        whenever(apiService.getSneakers()).thenReturn(emptyList())
        
        val result = repository.fetchRemoteSneakers()
        
        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull()?.isEmpty() == true)
    }

    @Test
    fun `fetchRemoteSneakers handles null image gracefully`() = runTest {
        val dtoWithNullImage = listOf(
            SneakerDto(
                id = 1L,
                marca = "Test",
                modelo = "Sneaker",
                talla = 42.0,
                tallas = listOf(42.0),
                color = "Red",
                precio = 100.0,
                stock = 5,
                image = null
            )
        )
        whenever(apiService.getSneakers()).thenReturn(dtoWithNullImage)
        
        val result = repository.fetchRemoteSneakers()
        
        assertTrue(result.isSuccess)
        assertEquals("", result.getOrNull()?.get(0)?.imageUrl)
    }

    @Test
    fun `fetchRemoteSneakers handles null tallas gracefully`() = runTest {
        val dtoWithNullTallas = listOf(
            SneakerDto(
                id = 1L,
                marca = "Test",
                modelo = "Sneaker",
                talla = 42.0,
                tallas = null,
                color = "Red",
                precio = 100.0,
                stock = 5,
                image = "https://example.com/test.jpg"
            )
        )
        whenever(apiService.getSneakers()).thenReturn(dtoWithNullTallas)
        
        val result = repository.fetchRemoteSneakers()
        
        assertTrue(result.isSuccess)
        // When tallas is null but talla exists, it uses talla as single size
        assertEquals(listOf(42), result.getOrNull()?.get(0)?.sizes)
    }
}
