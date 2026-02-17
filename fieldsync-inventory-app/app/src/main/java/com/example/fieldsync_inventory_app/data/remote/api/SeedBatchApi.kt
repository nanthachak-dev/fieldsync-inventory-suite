package com.example.fieldsync_inventory_app.data.remote.api

import com.example.fieldsync_inventory_app.data.remote.dto.seed_batch.SeedBatchRequestDto
import com.example.fieldsync_inventory_app.data.remote.dto.seed_batch.SeedBatchResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface SeedBatchApi {
    @GET("api/seed-batches")
    suspend fun getSeedBatches(): List<SeedBatchResponseDto>

    @POST("api/seed-batches")
    suspend fun createSeedBatch(
        @Body request: SeedBatchRequestDto
    ): SeedBatchResponseDto

    @PUT("api/seed-batches/{id}")
    suspend fun updateSeedBatch(
        @Path("id") id: Long,
        @Body request: SeedBatchRequestDto
    ): SeedBatchResponseDto

    @DELETE("api/seed-batches/{id}")
    suspend fun deleteSeedBatch(
        @Path("id") id: Int
    ): Response<Unit>

    @GET("api/seed-batches/{id}")
    suspend fun getSeedBatchById(
        @Path("id") id: Int
    ): SeedBatchResponseDto
}