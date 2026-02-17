package com.example.fieldsync_inventory_app.data.mapper

import com.example.fieldsync_inventory_app.data.local.entity.CustomerTypeEntity
import com.example.fieldsync_inventory_app.data.remote.dto.customer_type.CustomerTypeRequestDto
import com.example.fieldsync_inventory_app.data.remote.dto.customer_type.CustomerTypeResponseDto
import com.example.fieldsync_inventory_app.domain.model.CustomerType
import java.time.Instant

// -- Mapper --
// To map from API response to Room Entity
fun CustomerTypeResponseDto.toEntity(): CustomerTypeEntity {
    return CustomerTypeEntity(
        id = this.id,
        name = this.name,
        description = this.description,
        createdAt = Instant.parse(this.createdAt).toEpochMilli(),
        updatedAt = Instant.parse(this.updatedAt).toEpochMilli(),
        deletedAt = this.deletedAt?.let { Instant.parse(it).toEpochMilli() }
    )
}

// To map from API response to Domain Model
fun CustomerTypeResponseDto.toDomain(): CustomerType {
    return CustomerType(
        id = this.id,
        name = this.name,
        description = this.description,
        createdAt = Instant.parse(this.createdAt).toEpochMilli(),
        updatedAt = Instant.parse(this.updatedAt).toEpochMilli(),
        deletedAt = this.deletedAt?.let { Instant.parse(it).toEpochMilli() }
    )
}

// To map from Room Entity to Domain Model
fun CustomerTypeEntity.toDomain(): CustomerType {
    return CustomerType(
        id = this.id,
        name = this.name,
        description = this.description,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        deletedAt = this.deletedAt
    )
}

// Mapper to convert Domain Model to Request DTO (for PUT/POST)
fun CustomerType.toRequestDto(): CustomerTypeRequestDto {
    return CustomerTypeRequestDto(
        name = this.name,
        description = this.description
    )
}