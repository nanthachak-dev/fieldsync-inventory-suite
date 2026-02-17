package com.example.fieldsync_inventory_app.domain.use_case.inventory

import com.example.fieldsync_inventory_app.domain.model.inventory.Inventory
import com.example.fieldsync_inventory_app.domain.repository.inventory.InventoryRepository
import javax.inject.Inject

class GetInventoryUseCase @Inject constructor(
    private val repository: InventoryRepository
) {
    suspend operator fun invoke(
        lastDate: String? = null,
        page: Int? = null,
        size: Int? = null
    ): Inventory {
        return repository.getInventory(lastDate, page, size)
    }
}
