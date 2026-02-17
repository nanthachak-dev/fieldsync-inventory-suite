package com.example.fieldsync_inventory_app.domain.use_case.last_sync

import com.example.fieldsync_inventory_app.domain.repository.last_sync.LastSyncRepository
import javax.inject.Inject

class GetLastSyncByNameUseCase @Inject constructor(
    private val lastSyncRepository: LastSyncRepository
) {
    operator fun invoke(name: String) = lastSyncRepository.getLastSyncByName(name)
}
