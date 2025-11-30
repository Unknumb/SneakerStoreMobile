package com.example.appsneakerstore.model

data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val imageUrl: String,
    val description: String,
    val sizes: List<Int>,
    val rating: Float,
    val reviews: List<Review>,
    val stock: Int,
    val color: String
)

data class Review(
    val author: String,
    val rating: Float,
    val comment: String
)

data class Order(
    val items: List<Product>,
    val shippingAddress: String,
    val total: Double,
    val orderDate: String
)