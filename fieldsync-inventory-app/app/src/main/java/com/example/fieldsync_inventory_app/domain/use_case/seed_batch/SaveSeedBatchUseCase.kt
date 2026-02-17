package com.example.fieldsync_inventory_app.domain.use_case.seed_batch

import com.example.fieldsync_inventory_app.domain.repository.seed_batch.SeedBatchRepository
import com.example.fieldsync_inventory_app.domain.model.SeedBatch
import javax.inject.Inject

class SaveSeedBatchUseCase @Inject constructor(
    private val repository: SeedBatchRepository
) {
    suspend operator fun invoke(seedBatch: SeedBatch) {
        repository.saveSeedBatch(seedBatch)
    }
}
