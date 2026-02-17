package com.example.fieldsync_inventory_app.domain.repository.inventory

import com.example.fieldsync_inventory_app.data.mapper.toDomain
import com.example.fieldsync_inventory_app.data.remote.api.InventoryApi
import com.example.fieldsync_inventory_app.domain.model.inventory.Inventory
import com.example.fieldsync_inventory_app.domain.model.inventory.InventoryBatch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InventoryRepositoryImpl @Inject constructor(
    private val api: InventoryApi
) : InventoryRepository {

    override suspend fun getInventory(lastDate: String?, page: Int?, size: Int?): Inventory {
        return api.getInventory(lastDate, page, size).toDomain()
    }

    override suspend fun getInventoryBatch(id: Int, lastDate: String?, page: Int?, size: Int?): InventoryBatch {
        return api.getInventoryBatch(id, lastDate, page, size).toDomain()
    }
}
