package com.example.fieldsync_inventory_app.domain.use_case.seed_batch

import com.example.fieldsync_inventory_app.domain.repository.seed_batch.SeedBatchRepository
import javax.inject.Inject

class GetLocalSeedBatchesUseCase @Inject constructor(
    private val repository: SeedBatchRepository
) {
    operator fun invoke() = repository.getLocalSeedBatches()
}