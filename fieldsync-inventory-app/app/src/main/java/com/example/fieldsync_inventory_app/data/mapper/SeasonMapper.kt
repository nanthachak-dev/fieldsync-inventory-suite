package com.example.fieldsync_inventory_app.data.mapper

import com.example.fieldsync_inventory_app.data.remote.dto.season.SeasonRequestDto
import com.example.fieldsync_inventory_app.data.remote.dto.season.SeasonResponseDto
import java.time.Instant
import com.example.fieldsync_inventory_app.data.local.entity.SeasonEntity as SeasonEntity
import com.example.fieldsync_inventory_app.domain.model.Season as SeasonModel

// To map from API response to Room Entity
fun SeasonResponseDto.toEntity(): SeasonEntity {
    return SeasonEntity(
        id = this.id,
        name = this.name,
        description = this.description,
        createdAt = Instant.parse(this.createdAt).toEpochMilli(),
        updatedAt = Instant.parse(this.updatedAt).toEpochMilli(),
        deletedAt = this.deletedAt?.let { Instant.parse(it).toEpochMilli() }
    )
}

// To map from API response to Domain Model
fun SeasonResponseDto.toDomain(): SeasonModel {
    return SeasonModel(
        id = this.id,
        name = this.name,
        description = this.description,
        createdAt = Instant.parse(this.createdAt).toEpochMilli(),
        updatedAt = Instant.parse(this.updatedAt).toEpochMilli(),
        deletedAt = this.deletedAt?.let { Instant.parse(it).toEpochMilli() }
    )
}

// To map from Room Entity to Domain Model
fun SeasonEntity.toDomain(): SeasonModel {
    return SeasonModel(
        id = this.id,
        name = this.name,
        description = this.description,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        deletedAt = this.deletedAt
    )
}

// Mapper to convert Domain Model to Request DTO (for PUT/POST)
fun SeasonModel.toRequestDto(): SeasonRequestDto {
    return SeasonRequestDto(
        name = this.name,
        description = this.description
    )
}
