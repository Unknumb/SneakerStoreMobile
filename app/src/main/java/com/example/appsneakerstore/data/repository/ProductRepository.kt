package com.example.appsneakerstore.data.repository

import com.example.appsneakerstore.data.local.MockData
import com.example.appsneakerstore.data.remote.SneakerApiService
import com.example.appsneakerstore.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepository(
    private val api: SneakerApiService
) {

    // Por ahora, devuelve los productos mock
    fun getLocalProducts(): List<Product> = MockData.products

    // Ejemplo de función para llamar al backend (aún no usada en la UI)
    suspend fun fetchRemoteSneakers(): Result<List<Product>> = withContext(Dispatchers.IO) {
        return@withContext try {
            val remoteList = api.getSneakers()
            // TODO: mapear SneakerDto -> Product cuando quieras usar el backend de verdad
            Result.success(emptyList()) // de momento vacío para no romper nada
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}