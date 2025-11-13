package com.example.appsneakerstore.model

data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val imageUrl: String,
    val description: String,
    val sizes: List<Int>,
    val rating: Float,
    val reviews: List<Review>
)

data class Review(
    val author: String,
    val rating: Float,
    val comment: String
)