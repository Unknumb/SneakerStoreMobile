package com.example.appsneakerstore

import com.example.appsneakerstore.model.Order
import com.example.appsneakerstore.model.Product
import com.example.appsneakerstore.model.Review
import org.junit.Assert.*
import org.junit.Test

class ProductModelTest {

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
            reviews = emptyList()
        )
        
        assertEquals(1, product.id)
        assertEquals("Test Sneaker", product.name)
        assertEquals(99.99, product.price, 0.01)
        assertEquals("https://example.com/image.jpg", product.imageUrl)
        assertEquals("A test sneaker", product.description)
        assertEquals(listOf(40, 41, 42), product.sizes)
        assertEquals(4.5f, product.rating, 0.01f)
        assertTrue(product.reviews.isEmpty())
    }

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
            reviews = emptyList()
        )
        
        val product2 = Product(
            id = 1,
            name = "Sneaker",
            price = 100.0,
            imageUrl = "",
            description = "",
            sizes = listOf(42),
            rating = 4.0f,
            reviews = emptyList()
        )
        
        assertEquals(product1, product2)
    }

    @Test
    fun `Product with different ids are not equal`() {
        val product1 = Product(
            id = 1,
            name = "Sneaker",
            price = 100.0,
            imageUrl = "",
            description = "",
            sizes = listOf(42),
            rating = 4.0f,
            reviews = emptyList()
        )
        
        val product2 = product1.copy(id = 2)
        
        assertNotEquals(product1, product2)
    }

    @Test
    fun `Review data class creates correctly`() {
        val review = Review(
            author = "John Doe",
            rating = 5.0f,
            comment = "Great product!"
        )
        
        assertEquals("John Doe", review.author)
        assertEquals(5.0f, review.rating, 0.01f)
        assertEquals("Great product!", review.comment)
    }

    @Test
    fun `Product with reviews creates correctly`() {
        val reviews = listOf(
            Review("User1", 4.0f, "Good"),
            Review("User2", 5.0f, "Excellent")
        )
        
        val product = Product(
            id = 1,
            name = "Sneaker",
            price = 150.0,
            imageUrl = "",
            description = "",
            sizes = listOf(40, 41),
            rating = 4.5f,
            reviews = reviews
        )
        
        assertEquals(2, product.reviews.size)
        assertEquals("User1", product.reviews[0].author)
    }

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
                reviews = emptyList()
            ),
            Product(
                id = 2,
                name = "Sneaker 2",
                price = 150.0,
                imageUrl = "",
                description = "",
                sizes = listOf(43),
                rating = 4.5f,
                reviews = emptyList()
            )
        )
        
        val order = Order(
            items = products,
            shippingAddress = "123 Test Street",
            total = 250.0,
            orderDate = "2025-11-29"
        )
        
        assertEquals(2, order.items.size)
        assertEquals("123 Test Street", order.shippingAddress)
        assertEquals(250.0, order.total, 0.01)
        assertEquals("2025-11-29", order.orderDate)
    }

    @Test
    fun `Order total matches sum of products`() {
        val product1 = Product(
            id = 1,
            name = "Sneaker 1",
            price = 100.0,
            imageUrl = "",
            description = "",
            sizes = emptyList(),
            rating = 0f,
            reviews = emptyList()
        )
        val product2 = Product(
            id = 2,
            name = "Sneaker 2",
            price = 150.0,
            imageUrl = "",
            description = "",
            sizes = emptyList(),
            rating = 0f,
            reviews = emptyList()
        )
        
        val calculatedTotal = product1.price + product2.price
        
        val order = Order(
            items = listOf(product1, product2),
            shippingAddress = "Test",
            total = calculatedTotal,
            orderDate = "2025-11-29"
        )
        
        assertEquals(250.0, order.total, 0.01)
    }

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
            reviews = emptyList()
        )
        
        val modified = original.copy(price = 120.0, name = "Modified")
        
        assertEquals(1, modified.id)
        assertEquals("Modified", modified.name)
        assertEquals(120.0, modified.price, 0.01)
        assertEquals("Original", original.name) // Original unchanged
    }

    @Test
    fun `Product hashCode is consistent`() {
        val product1 = Product(
            id = 1,
            name = "Sneaker",
            price = 100.0,
            imageUrl = "",
            description = "",
            sizes = listOf(42),
            rating = 4.0f,
            reviews = emptyList()
        )
        
        val product2 = Product(
            id = 1,
            name = "Sneaker",
            price = 100.0,
            imageUrl = "",
            description = "",
            sizes = listOf(42),
            rating = 4.0f,
            reviews = emptyList()
        )
        
        assertEquals(product1.hashCode(), product2.hashCode())
    }

    @Test
    fun `Empty sizes list is valid`() {
        val product = Product(
            id = 1,
            name = "Test",
            price = 50.0,
            imageUrl = "",
            description = "",
            sizes = emptyList(),
            rating = 0f,
            reviews = emptyList()
        )
        
        assertTrue(product.sizes.isEmpty())
    }

    @Test
    fun `Product rating can be zero`() {
        val product = Product(
            id = 1,
            name = "New Product",
            price = 80.0,
            imageUrl = "",
            description = "",
            sizes = listOf(40),
            rating = 0f,
            reviews = emptyList()
        )
        
        assertEquals(0f, product.rating, 0.01f)
    }
}
