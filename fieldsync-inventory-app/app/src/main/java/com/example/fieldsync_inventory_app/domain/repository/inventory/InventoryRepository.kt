package com.example.fieldsync_inventory_app.domain.repository.inventory

import com.example.fieldsync_inventory_app.domain.model.inventory.Inventory
import com.example.fieldsync_inventory_app.domain.model.inventory.InventoryBatch

interface InventoryRepository {
    suspend fun getInventory(lastDate: String? = null, page: Int? = null, size: Int? = null): Inventory
    suspend fun getInventoryBatch(id: Int, lastDate: String? = null, page: Int? = null, size: Int? = null): InventoryBatch
}
