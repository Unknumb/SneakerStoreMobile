package com.example.appsneakerstore.data.remote

import com.example.appsneakerstore.model.Product
import com.example.appsneakerstore.model.Review

fun SneakerDto.toProduct(): Product {
    // Obtener tallas: usar lista de tallas si existe, sino usar talla individual
    val sizesList = when {
        !this.tallas.isNullOrEmpty() -> this.tallas.map { it.toInt() }
        this.talla != null -> listOf(this.talla.toInt())
        else -> emptyList()
    }
    
    return Product(
        id = (this.id ?: 0L).toInt(),
        name = "${this.marca} ${this.modelo}",
        price = this.precio,
        imageUrl = this.image ?: "",
        description = this.color ?: "Sin descripción",
        sizes = sizesList,
        rating = 4.5f,
        reviews = emptyList<Review>(),
        stock = this.stock,
        color = this.color ?: "No especificado"
    )
}