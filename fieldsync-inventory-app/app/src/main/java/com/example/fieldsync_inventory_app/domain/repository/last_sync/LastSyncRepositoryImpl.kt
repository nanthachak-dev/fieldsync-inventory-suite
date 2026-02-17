package com.example.fieldsync_inventory_app.domain.repository.last_sync

import com.example.fieldsync_inventory_app.data.local.dao.LastSyncDao
import com.example.fieldsync_inventory_app.data.mapper.toDomain
import com.example.fieldsync_inventory_app.data.mapper.toEntity
import com.example.fieldsync_inventory_app.domain.model.LastSync
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LastSyncRepositoryImpl @Inject constructor(
    private val dao: LastSyncDao
) : LastSyncRepository {
    override suspend fun insertAll(lastSyncs: List<LastSync>) {
        dao.insertAll(lastSyncs.map { it.toEntity() })
    }

    override fun getLastSyncByName(name: String): Flow<LastSync?> {
        return dao.getByName(name).map { entity -> entity?.toDomain() }
    }
}
