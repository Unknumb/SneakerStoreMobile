package com.example.appsneakerstore.data.remote

import retrofit2.http.*

data class SneakerDto(
    val id: Long?,
    val marca: String,
    val modelo: String,
    val talla: Double,
    val color: String,
    val precio: Double,
    val stock: Int
)

interface SneakerApiService {

    @GET("api/sneakers")
    suspend fun getSneakers(): List<SneakerDto>

    @GET("api/sneakers/{id}")
    suspend fun getSneakerById(@Path("id") id: Long): SneakerDto

    @POST("api/sneakers")
    suspend fun createSneaker(@Body sneaker: SneakerDto): SneakerDto

    @DELETE("api/sneakers/{id}")
    suspend fun deleteSneaker(@Path("id") id: Long)

    @PATCH("api/sneakers/{id}/stock")
    suspend fun updateStock(
        @Path("id") id: Long,
        @Query("cantidad") cantidad: Int
    ): SneakerDto
}