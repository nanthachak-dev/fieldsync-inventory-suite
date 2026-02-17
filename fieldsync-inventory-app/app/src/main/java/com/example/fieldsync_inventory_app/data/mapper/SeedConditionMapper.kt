package com.example.fieldsync_inventory_app.data.mapper

import com.example.fieldsync_inventory_app.data.remote.dto.seed_condition.SeedConditionRequestDto
import com.example.fieldsync_inventory_app.data.remote.dto.seed_condition.SeedConditionResponseDto
import java.time.Instant
import com.example.fieldsync_inventory_app.data.local.entity.SeedConditionEntity as SeedConditionEntity
import com.example.fieldsync_inventory_app.domain.model.SeedCondition as SeedConditionModel

// To map from API response to Room Entity
fun SeedConditionResponseDto.toEntity(): SeedConditionEntity {
    return SeedConditionEntity(
        id = this.id,
        name = this.name,
        description = this.description,
        createdAt = Instant.parse(this.createdAt).toEpochMilli(),
        updatedAt = Instant.parse(this.updatedAt).toEpochMilli(),
        deletedAt = this.deletedAt?.let { Instant.parse(it).toEpochMilli() }
    )
}

// To map from API response to Domain Model
fun SeedConditionResponseDto.toDomain(): SeedConditionModel {
    return SeedConditionModel(
        id = this.id,
        name = this.name,
        description = this.description,
        createdAt = Instant.parse(this.createdAt).toEpochMilli(),
        updatedAt = Instant.parse(this.updatedAt).toEpochMilli(),
        deletedAt = this.deletedAt?.let { Instant.parse(it).toEpochMilli() }
    )
}

// To map from Room Entity to Domain Model
fun SeedConditionEntity.toDomain(): SeedConditionModel {
    return SeedConditionModel(
        id = this.id,
        name = this.name,
        description = this.description,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        deletedAt = this.deletedAt
    )
}

// Mapper to convert Domain Model to Request DTO (for PUT/POST)
fun SeedConditionModel.toRequestDto(): SeedConditionRequestDto {
    return SeedConditionRequestDto(
        name = this.name,
        description = this.description
    )
}
