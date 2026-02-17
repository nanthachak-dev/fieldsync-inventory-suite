package com.example.fieldsync_inventory_app.domain.use_case.seed_batch

import com.example.fieldsync_inventory_app.domain.repository.seed_batch.SeedBatchRepository
import javax.inject.Inject

class DeleteSeedBatchUseCase @Inject constructor(
    private val repository: SeedBatchRepository
) {
    suspend operator fun invoke(id: Int) {
        repository.deleteSeedBatch(id)
    }
}
