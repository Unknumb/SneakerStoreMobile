package com.example.appsneakerstore

import com.example.appsneakerstore.data.remote.SneakerDto
import com.example.appsneakerstore.data.remote.toProduct
import org.junit.Assert.*
import org.junit.Test

class SneakerMapperTest {

    @Test
    fun `toProduct maps all fields correctly`() {
        val dto = SneakerDto(
            id = 1L,
            marca = "Nike",
            modelo = "Air Max",
            talla = 42.0,
            tallas = listOf(40.0, 41.0, 42.0, 43.0),
            color = "Blanco",
            precio = 149.99,
            stock = 10,
            image = "https://example.com/nike.jpg"
        )
        
        val product = dto.toProduct()
        
        assertEquals(1, product.id)
        assertEquals("Nike Air Max", product.name)
        assertEquals(149.99, product.price, 0.01)
        assertEquals("Blanco", product.description) // color is mapped to description
        assertEquals("https://example.com/nike.jpg", product.imageUrl)
        assertEquals(listOf(40, 41, 42, 43), product.sizes)
    }

    @Test
    fun `toProduct handles null image with empty string`() {
        val dto = SneakerDto(
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
        
        val product = dto.toProduct()
        
        assertEquals("", product.imageUrl)
    }

    @Test
    fun `toProduct handles null tallas with single talla`() {
        val dto = SneakerDto(
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
        
        val product = dto.toProduct()
        
        // When tallas is null but talla exists, uses talla as single size
        assertEquals(listOf(42), product.sizes)
    }

    @Test
    fun `toProduct handles both null tallas and null talla`() {
        val dto = SneakerDto(
            id = 1L,
            marca = "Test",
            modelo = "Sneaker",
            talla = null,
            tallas = null,
            color = "Red",
            precio = 100.0,
            stock = 5,
            image = "https://example.com/test.jpg"
        )
        
        val product = dto.toProduct()
        
        assertTrue(product.sizes.isEmpty())
    }

    @Test
    fun `toProduct converts double tallas to int sizes`() {
        val dto = SneakerDto(
            id = 1L,
            marca = "Brand",
            modelo = "Model",
            talla = 42.5,
            tallas = listOf(39.5, 40.0, 41.5, 42.0),
            color = "Blue",
            precio = 100.0,
            stock = 5,
            image = ""
        )
        
        val product = dto.toProduct()
        
        // Doubles are converted to Int, truncating decimals
        assertEquals(listOf(39, 40, 41, 42), product.sizes)
    }

    @Test
    fun `toProduct default rating is 4_5f`() {
        val dto = SneakerDto(
            id = 1L,
            marca = "Brand",
            modelo = "Model",
            talla = 42.0,
            tallas = listOf(42.0),
            color = "Black",
            precio = 100.0,
            stock = 5,
            image = ""
        )
        
        val product = dto.toProduct()
        
        assertEquals(4.5f, product.rating, 0.01f)
    }

    @Test
    fun `toProduct default reviews is empty list`() {
        val dto = SneakerDto(
            id = 1L,
            marca = "Brand",
            modelo = "Model",
            talla = 42.0,
            tallas = listOf(42.0),
            color = "White",
            precio = 100.0,
            stock = 5,
            image = ""
        )
        
        val product = dto.toProduct()
        
        assertTrue(product.reviews.isEmpty())
    }

    @Test
    fun `toProduct with empty string image keeps empty string`() {
        val dto = SneakerDto(
            id = 1L,
            marca = "Brand",
            modelo = "Model",
            talla = 42.0,
            tallas = listOf(42.0),
            color = "Green",
            precio = 100.0,
            stock = 5,
            image = ""
        )
        
        val product = dto.toProduct()
        
        assertEquals("", product.imageUrl)
    }

    @Test
    fun `toProduct handles zero price`() {
        val dto = SneakerDto(
            id = 1L,
            marca = "Free",
            modelo = "Sneaker",
            talla = 42.0,
            tallas = listOf(42.0),
            color = "Any",
            precio = 0.0,
            stock = 5,
            image = "https://example.com/free.jpg"
        )
        
        val product = dto.toProduct()
        
        assertEquals(0.0, product.price, 0.01)
    }

    @Test
    fun `toProduct handles null color with default description`() {
        val dto = SneakerDto(
            id = 1L,
            marca = "No",
            modelo = "Color",
            talla = 42.0,
            tallas = listOf(42.0),
            color = null,
            precio = 50.0,
            stock = 5,
            image = "https://example.com/nodesc.jpg"
        )
        
        val product = dto.toProduct()
        
        assertEquals("Sin descripción", product.description)
    }

    @Test
    fun `toProduct handles single size in tallas`() {
        val dto = SneakerDto(
            id = 1L,
            marca = "Limited",
            modelo = "Edition",
            talla = 42.0,
            tallas = listOf(42.0),
            color = "Gold",
            precio = 200.0,
            stock = 1,
            image = "https://example.com/limited.jpg"
        )
        
        val product = dto.toProduct()
        
        assertEquals(1, product.sizes.size)
        assertEquals(42, product.sizes[0])
    }

    @Test
    fun `toProduct preserves id correctly`() {
        val dto = SneakerDto(
            id = 999L,
            marca = "High",
            modelo = "ID",
            talla = 42.0,
            tallas = listOf(42.0),
            color = "Purple",
            precio = 100.0,
            stock = 5,
            image = ""
        )
        
        val product = dto.toProduct()
        
        assertEquals(999, product.id)
    }

    @Test
    fun `toProduct handles null id with 0`() {
        val dto = SneakerDto(
            id = null,
            marca = "No",
            modelo = "ID",
            talla = 42.0,
            tallas = listOf(42.0),
            color = "Gray",
            precio = 100.0,
            stock = 5,
            image = ""
        )
        
        val product = dto.toProduct()
        
        assertEquals(0, product.id)
    }

    @Test
    fun `toProduct combines marca and modelo for name`() {
        val dto = SneakerDto(
            id = 1L,
            marca = "Puma",
            modelo = "RS-X",
            talla = 42.0,
            tallas = listOf(42.0),
            color = "Black/White",
            precio = 120.0,
            stock = 5,
            image = ""
        )
        
        val product = dto.toProduct()
        
        assertEquals("Puma RS-X", product.name)
    }
}
