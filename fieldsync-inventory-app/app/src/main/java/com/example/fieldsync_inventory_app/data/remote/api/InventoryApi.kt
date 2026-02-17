package com.example.fieldsync_inventory_app.data.remote.api

import com.example.fieldsync_inventory_app.data.remote.dto.inventory.InventoryBatchResponseDto
import com.example.fieldsync_inventory_app.data.remote.dto.inventory.InventoryResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface InventoryApi {

    @GET("api/v1/inventory")
    suspend fun getInventory(
        @Query("lastDate") lastDate: String? = null,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null
    ): InventoryResponseDto

    @GET("api/v1/inventory/batch/{id}")
    suspend fun getInventoryBatch(
        @Path("id") id: Int,
        @Query("lastDate") lastDate: String? = null,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null
    ): InventoryBatchResponseDto
}
