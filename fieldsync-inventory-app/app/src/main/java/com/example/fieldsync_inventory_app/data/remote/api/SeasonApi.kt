package com.example.fieldsync_inventory_app.data.remote.api

import com.example.fieldsync_inventory_app.data.remote.dto.season.SeasonRequestDto
import com.example.fieldsync_inventory_app.data.remote.dto.season.SeasonResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface SeasonApi {
    @GET("api/seasons")
    suspend fun getSeasons(): List<SeasonResponseDto>

    // POST request to create a new season
    @POST("api/seasons")
    suspend fun createSeason(
        @Body request: SeasonRequestDto
    ): SeasonResponseDto

    // PUT request to update an existing season
    @PUT("api/seasons/{id}")
    suspend fun updateSeason(
        @Path("id") id: Int,
        @Body request: SeasonRequestDto
    ): SeasonResponseDto

    // DELETE request to delete a season
    @DELETE("api/seasons/{id}")
    suspend fun deleteSeason(
        @Path("id") id: Int
    ): Response<Unit> // Use Response<Unit> for a successful empty body response

    // GET request to get a specific season by ID
    @GET("api/seasons/{id}")
    suspend fun getSeasonById(
        @Path("id") id: Int
    ): SeasonResponseDto

    // GET request to list soft-deleted seasons
    @GET("api/seasons/deleted")
    suspend fun getDeletedSeasons(): List<SeasonResponseDto>

    // GET request to list all seasons including deleted ones
    @GET("api/seasons/all")
    suspend fun getAllSeasons(): List<SeasonResponseDto>
}
