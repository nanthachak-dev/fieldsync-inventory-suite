package com.example.fieldsync_inventory_app.data.remote.api

import com.example.fieldsync_inventory_app.data.remote.dto.rice_generation.RiceGenerationRequestDto
import com.example.fieldsync_inventory_app.data.remote.dto.rice_generation.RiceGenerationResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RiceGenerationApi {
    @GET("api/rice-generations")
    suspend fun getRiceGenerations(): List<RiceGenerationResponseDto>

    // POST request to create a new rice generation
    @POST("api/rice-generations")
    suspend fun createRiceGeneration(
        @Body request: RiceGenerationRequestDto
    ): RiceGenerationResponseDto

    // PUT request to update an existing rice generation
    @PUT("api/rice-generations/{id}")
    suspend fun updateRiceGeneration(
        @Path("id") id: Int,
        @Body request: RiceGenerationRequestDto
    ): RiceGenerationResponseDto

    // DELETE request to delete a rice generation
    @DELETE("api/rice-generations/{id}")
    suspend fun deleteRiceGeneration(
        @Path("id") id: Int
    ): Response<Unit> // Use Response<Unit> for a successful empty body response

    // GET request to get a specific rice generation by ID
    @GET("api/rice-generations/{id}")
    suspend fun getRiceGenerationById(
        @Path("id") id: Int
    ): RiceGenerationResponseDto
}
