package com.example.fieldsync_inventory_app.data.mapper

import com.example.fieldsync_inventory_app.data.remote.dto.rice_generation.RiceGenerationRequestDto
import com.example.fieldsync_inventory_app.data.remote.dto.rice_generation.RiceGenerationResponseDto
import java.time.Instant
import com.example.fieldsync_inventory_app.data.local.entity.RiceGenerationEntity as RiceGenerationEntity
import com.example.fieldsync_inventory_app.domain.model.RiceGeneration as RiceGenerationModel

// To map from API response to Room Entity
fun RiceGenerationResponseDto.toEntity(): RiceGenerationEntity {
    return RiceGenerationEntity(
        id = this.id,
        name = this.name,
        description = this.description,
        createdAt = Instant.parse(this.createdAt).toEpochMilli(),
        updatedAt = Instant.parse(this.updatedAt).toEpochMilli(),
        deletedAt = this.deletedAt?.let { Instant.parse(it).toEpochMilli() }
    )
}

// To map from API response to Domain Model
fun RiceGenerationResponseDto.toDomain(): RiceGenerationModel {
    return RiceGenerationModel(
        id = this.id,
        name = this.name,
        description = this.description,
        createdAt = Instant.parse(this.createdAt).toEpochMilli(),
        updatedAt = Instant.parse(this.updatedAt).toEpochMilli(),
        deletedAt = this.deletedAt?.let { Instant.parse(it).toEpochMilli() }
    )
}

// To map from Room Entity to Domain Model
fun RiceGenerationEntity.toDomain(): RiceGenerationModel {
    return RiceGenerationModel(
        id = this.id,
        name = this.name,
        description = this.description,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        deletedAt = this.deletedAt
    )
}

// Mapper to convert Domain Model to Request DTO (for PUT/POST)
fun RiceGenerationModel.toRequestDto(): RiceGenerationRequestDto {
    return RiceGenerationRequestDto(
        name = this.name,
        description = this.description
    )
}
