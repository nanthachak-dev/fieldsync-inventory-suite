package com.example.fieldsync_inventory_app.data.remote.api

import com.example.fieldsync_inventory_app.data.remote.dto.rice_variety.RiceVarietyRequestDto
import com.example.fieldsync_inventory_app.data.remote.dto.rice_variety.RiceVarietyResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RiceVarietyApi {
    @GET("api/rice-varieties")
    suspend fun getRiceVarieties(): List<RiceVarietyResponseDto>

    // POST request to create a new rice variety
    @POST("api/rice-varieties")
    suspend fun createRiceVariety(
        @Body request: RiceVarietyRequestDto
    ): RiceVarietyResponseDto

    // PUT request to update an existing rice variety
    @PUT("api/rice-varieties/{id}")
    suspend fun updateRiceVariety(
        @Path("id") id: Int,
        @Body request: RiceVarietyRequestDto
    ): RiceVarietyResponseDto

    // DELETE request to delete a rice variety
    @DELETE("api/rice-varieties/{id}")
    suspend fun deleteRiceVariety(
        @Path("id") id: Int
    ): Response<Unit> // Use Response<Unit> for a successful empty body response
}
