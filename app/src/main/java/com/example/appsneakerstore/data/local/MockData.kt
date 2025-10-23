package com.example.appsneakerstore.data.local
import com.example.appsneakerstore.model.Product
object MockData {
    val products = listOf(
        Product(
            id = 1,
            name = "Air Max 90",
            price = 129.99,
            imageUrl = "https://example.com/airmax90.jpg",
            description = "Zapatillas clásicas con amortiguación Air visible."
        ),
        Product(
            id = 2,
            name = "Jordan 1 Retro",
            price = 149.99,
            imageUrl = "https://example.com/jordan1.jpg",
            description = "Modelo icónico de la línea Air Jordan."
        ),
        Product(
            id = 3,
            name = "Yeezy Boost 350",
            price = 199.99,
            imageUrl = "https://example.com/yeezy350.jpg",
            description = "Diseño moderno con tejido Primeknit."
        )
    )
}