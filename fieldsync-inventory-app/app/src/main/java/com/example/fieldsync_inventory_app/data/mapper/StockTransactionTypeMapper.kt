package com.example.fieldsync_inventory_app.data.mapper

import com.example.fieldsync_inventory_app.data.local.entity.StockTransactionTypeEntity
import com.example.fieldsync_inventory_app.data.remote.dto.transaction_type.StockTransactionTypeRequestDto
import com.example.fieldsync_inventory_app.data.remote.dto.transaction_type.StockTransactionTypeResponseDto
import com.example.fieldsync_inventory_app.domain.model.StockTransactionType
import java.time.Instant

// To map from API response to Entity
fun StockTransactionTypeResponseDto.toEntity(): StockTransactionTypeEntity {
    return StockTransactionTypeEntity(
        id = this.id,
        name = this.name,
        description = this.description,
        createdAt = Instant.parse(this.createdAt).toEpochMilli(),
        updatedAt = Instant.parse(this.updatedAt).toEpochMilli(),
        deletedAt = this.deletedAt?.let { Instant.parse(it).toEpochMilli() }
    )
}

// To map from API response to Domain Model
fun StockTransactionTypeResponseDto.toDomain(): StockTransactionType {
    return StockTransactionType(
        id = this.id,
        name = this.name,
        description = this.description,
        createdAt = Instant.parse(this.createdAt).toEpochMilli(),
        updatedAt = Instant.parse(this.updatedAt).toEpochMilli(),
        deletedAt = this.deletedAt?.let { Instant.parse(it).toEpochMilli() }
    )
}

// To map from Room Entity to Domain Model
fun StockTransactionTypeEntity.toDomain(): StockTransactionType {
    return StockTransactionType(
        id = this.id,
        name = this.name,
        description = this.description,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        deletedAt = this.deletedAt
    )
}

// Mapper to convert Domain Model to Request DTO (for PUT/POST)
fun StockTransactionType.toRequestDto(): StockTransactionTypeRequestDto {
    return StockTransactionTypeRequestDto(
        name = this.name,
        description = this.description
    )
}