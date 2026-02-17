package com.example.fieldsync_inventory_app.data.remote.api

import com.example.fieldsync_inventory_app.data.remote.dto.seed_condition.SeedConditionRequestDto
import com.example.fieldsync_inventory_app.data.remote.dto.seed_condition.SeedConditionResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface SeedConditionApi {
    @GET("api/seed-conditions")
    suspend fun getSeedConditions(): List<SeedConditionResponseDto>

    // POST request to create a new seed condition
    @POST("api/seed-conditions")
    suspend fun createSeedCondition(
        @Body request: SeedConditionRequestDto
    ): SeedConditionResponseDto

    // PUT request to update an existing seed condition
    @PUT("api/seed-conditions/{id}")
    suspend fun updateSeedCondition(
        @Path("id") id: Int,
        @Body request: SeedConditionRequestDto
    ): SeedConditionResponseDto

    // DELETE request to delete a seed condition
    @DELETE("api/seed-conditions/{id}")
    suspend fun deleteSeedCondition(
        @Path("id") id: Int
    ): Response<Unit> // Use Response<Unit> for a successful empty body response

    // GET request to get a specific seed condition by ID
    @GET("api/seed-conditions/{id}")
    suspend fun getSeedConditionById(
        @Path("id") id: Int
    ): SeedConditionResponseDto
}
