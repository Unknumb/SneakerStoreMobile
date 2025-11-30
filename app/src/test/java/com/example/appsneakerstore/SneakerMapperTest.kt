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
        assertEquals("Blanco", product.color)
        assertEquals("Blanco", product.description) // color is mapped to description
        assertEquals("https://example.com/nike.jpg", product.imageUrl)
        assertEquals(listOf(40, 41, 42, 43), product.sizes)
        assertEquals(10, product.stock)
    }
    
    @Test
    fun `toProduct maps stock and color correctly`() {
        val dto = SneakerDto(
            id = 1L,
            marca = "Nike",
            modelo = "Air Max",
            talla = 42.0,
            tallas = listOf(40.0),
            color = "Rojo",
            precio = 100.0,
            stock = 50,
            image = ""
        )
        
        val product = dto.toProduct()
        
        assertEquals(50, product.stock)
        assertEquals("Rojo", product.color)
    }
    
    @Test
    fun `toProduct uses default color when null`() {
        val dto = SneakerDto(
            id = 1L,
            marca = "Nike",
            modelo = "Air Max",
            talla = 42.0,
            tallas = listOf(40.0),
            color = null,
            precio = 100.0,
            stock = 50,
            image = ""
        )
        
        val product = dto.toProduct()
        
        assertEquals("No especificado", product.color)
        assertEquals("Sin descripción", product.description)
    }
}
