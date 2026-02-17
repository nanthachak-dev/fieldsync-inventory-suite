package com.example.fieldsync_inventory_app.domain.use_case.inventory

import com.example.fieldsync_inventory_app.domain.model.inventory.InventoryBatch
import com.example.fieldsync_inventory_app.domain.repository.inventory.InventoryRepository
import javax.inject.Inject

class GetInventoryBatchUseCase @Inject constructor(
    private val repository: InventoryRepository
) {
    suspend operator fun invoke(
        id: Int,
        lastDate: String? = null,
        page: Int? = null,
        size: Int? = null
    ): InventoryBatch {
        return repository.getInventoryBatch(id, lastDate, page, size)
    }
}
