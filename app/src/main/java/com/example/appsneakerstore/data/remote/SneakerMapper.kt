package com.example.appsneakerstore.data.remote

import com.example.appsneakerstore.model.Product
import com.example.appsneakerstore.model.Review

fun SneakerDto.toProduct(): Product {
    return Product(
        id = (this.id ?: 0L).toInt(),
        name = "${this.marca} ${this.modelo}",
        price = this.precio,
        imageUrl = "", // más adelante podrías añadir URLs reales desde el backend
        description = "Talla: ${this.talla} - Color: ${this.color}",
        sizes = listOf(this.talla.toInt()),
        rating = 4.5f, // valor “dummy” por ahora
        reviews = emptyList<Review>() // sin reviews desde el backend de momento
    )
}