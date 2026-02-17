package com.example.fieldsync_inventory_app.data.mapper

import com.example.fieldsync_inventory_app.data.local.entity.LastSyncEntity
import com.example.fieldsync_inventory_app.domain.model.LastSync

fun LastSyncEntity.toDomain(): LastSync{
    return LastSync(
        id = this.id,
        name = this.name,
        description = this.description,
        lastSyncTime = this.lastSyncTime
    )
}

fun LastSync.toEntity(): LastSyncEntity{
    return LastSyncEntity(
        id = this.id,
        name = this.name,
        description = this.description,
        lastSyncTime = this.lastSyncTime
    )
}