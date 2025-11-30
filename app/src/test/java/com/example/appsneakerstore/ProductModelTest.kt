package com.example.appsneakerstore

import com.example.appsneakerstore.model.Order
import com.example.appsneakerstore.model.Product
import com.example.appsneakerstore.model.Review
import org.junit.Assert.*
import org.junit.Test

class ProductModelTest {

    // Verifica que la clase de datos Product se cree correctamente con todos sus campos
    @Test
    fun `Product data class creates correctly`() {
        val product = Product(
            id = 1,
            name = "Test Sneaker",
            price = 99.99,
            imageUrl = "https://example.com/image.jpg",
            description = "A test sneaker",
            sizes = listOf(40, 41, 42),
            rating = 4.5f,
            reviews = emptyList(),
            stock = 10,
            color = "Red"
        )
        
        assertEquals(1, product.id)
        assertEquals("Test Sneaker", product.name)
        assertEquals(99.99, product.price, 0.01)
        assertEquals("https://example.com/image.jpg", product.imageUrl)
        assertEquals("A test sneaker", product.description)
        assertEquals(listOf(40, 41, 42), product.sizes)
        assertEquals(4.5f, product.rating, 0.01f)
        assertTrue(product.reviews.isEmpty())
        assertEquals(10, product.stock)
        assertEquals("Red", product.color)
    }

    // Verifica que dos objetos Product con los mismos datos sean considerados iguales (método equals)
    @Test
    fun `Product equality works correctly`() {
        val product1 = Product(
            id = 1,
            name = "Sneaker",
            price = 100.0,
            imageUrl = "",
            description = "",
            sizes = listOf(42),
            rating = 4.0f,
            reviews = emptyList(),
            stock = 5,
            color = "Blue"
        )
        
        val product2 = Product(
            id = 1,
            name = "Sneaker",
            price = 100.0,
            imageUrl = "",
            description = "",
            sizes = listOf(42),
            rating = 4.0f,
            reviews = emptyList(),
            stock = 5,
            color = "Blue"
        )
        
        assertEquals(product1, product2)
    }

    // Verifica que el método copy genere una nueva instancia modificando solo los campos especificados
    @Test
    fun `Product copy creates new instance with modified fields`() {
        val original = Product(
            id = 1,
            name = "Original",
            price = 100.0,
            imageUrl = "",
            description = "",
            sizes = listOf(42),
            rating = 4.0f,
            reviews = emptyList(),
            stock = 5,
            color = "Blue"
        )
        
        val modified = original.copy(price = 120.0, name = "Modified")
        
        assertEquals(1, modified.id)
        assertEquals("Modified", modified.name)
        assertEquals(120.0, modified.price, 0.01)
        assertEquals("Original", original.name) // Original sin cambios
    }
    
    // Verifica que la clase de datos Order se cree correctamente con la lista de items y total
    @Test
    fun `Order data class creates correctly`() {
        val products = listOf(
            Product(
                id = 1,
                name = "Sneaker 1",
                price = 100.0,
                imageUrl = "",
                description = "",
                sizes = listOf(42),
                rating = 4.0f,
                reviews = emptyList(),
                stock = 10,
                color = "Red"
            )
        )
        
        val order = Order(
            items = products,
            shippingAddress = "123 Test Street",
            total = 100.0,
            orderDate = "2025-11-29"
        )
        
        assertEquals(1, order.items.size)
        assertEquals("123 Test Street", order.shippingAddress)
        assertEquals(100.0, order.total, 0.01)
    }
}
