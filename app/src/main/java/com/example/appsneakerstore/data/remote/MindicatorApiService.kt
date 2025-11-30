package com.example.appsneakerstore.data.remote

import retrofit2.http.GET

data class MindicatorResponse(
    val dolar: IndicatorData
)

data class IndicatorData(
    val valor: Double
)

interface MindicatorApiService {
    @GET("api")
    suspend fun getIndicators(): MindicatorResponse
}