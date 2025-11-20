package com.example.appsneakerstore.data.repository

import com.example.appsneakerstore.data.local.MockData
import com.example.appsneakerstore.data.remote.SneakerApiService
import com.example.appsneakerstore.data.remote.toProduct
import com.example.appsneakerstore.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepository(
    private val api: SneakerApiService
) {

    // Fuente local actual (MockData)
    fun getLocalProducts(): List<Product> = MockData.products

    // Llamada real al backend
    suspend fun fetchRemoteSneakers(): Result<List<Product>> = withContext(Dispatchers.IO) {
        return@withContext try {
            val remoteList = api.getSneakers()
            val products = remoteList.map { it.toProduct() }
            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}