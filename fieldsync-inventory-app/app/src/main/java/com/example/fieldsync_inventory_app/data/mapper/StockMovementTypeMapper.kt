package com.example.fieldsync_inventory_app.data.mapper

import com.example.fieldsync_inventory_app.data.remote.dto.stock_movement_type.StockMovementTypeRequestDto
import com.example.fieldsync_inventory_app.data.remote.dto.stock_movement_type.StockMovementTypeResponseDto
import com.example.fieldsync_inventory_app.domain.model.StockMovementType
import java.time.Instant
import com.example.fieldsync_inventory_app.data.local.entity.StockMovementTypeEntity as StockMovementTypeEntity

// To map from API response to Entity
fun StockMovementTypeResponseDto.toEntity(): StockMovementTypeEntity {
    return StockMovementTypeEntity(
        id = this.id,
        name = this.name,
        effectOnStock = this.effectOnStock,
        description = this.description,
        createdAt = Instant.parse(this.createdAt).toEpochMilli(),
        updatedAt = Instant.parse(this.updatedAt).toEpochMilli(),
        deletedAt = this.deletedAt?.let { Instant.parse(it).toEpochMilli() }
    )
}

// To map from API response to Domain Model
fun StockMovementTypeResponseDto.toDomain(): StockMovementType {
    return StockMovementType(
        id = this.id,
        name = this.name,
        effectOnStock = this.effectOnStock,
        description = this.description,
        createdAt = Instant.parse(this.createdAt).toEpochMilli(),
        updatedAt = Instant.parse(this.updatedAt).toEpochMilli(),
        deletedAt = this.deletedAt?.let { Instant.parse(it).toEpochMilli() }
    )
}

// To map from Room Entity to Domain Model
fun StockMovementTypeEntity.toDomain(): StockMovementType {
    return StockMovementType(
        id = this.id,
        name = this.name,
        effectOnStock = this.effectOnStock,
        description = this.description,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        deletedAt = this.deletedAt
    )
}

// Mapper to convert Domain Model to Request DTO (for PUT/POST)
fun StockMovementType.toRequestDto(): StockMovementTypeRequestDto {
    return StockMovementTypeRequestDto(
        name = this.name,
        effectOnStock = this.effectOnStock,
        description = this.description
    )
}