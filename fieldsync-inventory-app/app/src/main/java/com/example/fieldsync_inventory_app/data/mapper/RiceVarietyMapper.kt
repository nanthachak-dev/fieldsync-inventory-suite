package com.example.fieldsync_inventory_app.data.mapper

import com.example.fieldsync_inventory_app.data.remote.dto.rice_variety.RiceVarietyRequestDto
import com.example.fieldsync_inventory_app.data.remote.dto.rice_variety.RiceVarietyResponseDto
import java.time.Instant
import com.example.fieldsync_inventory_app.data.local.entity.RiceVarietyEntity as RiceVarietyEntity
import com.example.fieldsync_inventory_app.domain.model.RiceVariety as RiceVarietyModel

// To map from API response to Room Entity
fun RiceVarietyResponseDto.toEntity(): RiceVarietyEntity {
    return RiceVarietyEntity(
        id = this.id,
        name = this.name,
        description = this.description,
        imageUrl = this.imageUrl,
        createdAt = Instant.parse(this.createdAt).toEpochMilli(),
        updatedAt = Instant.parse(this.updatedAt).toEpochMilli(),
        deletedAt = this.deletedAt?.let { Instant.parse(it).toEpochMilli() }
    )
}

// To map from API response to Domain Model
fun RiceVarietyResponseDto.toDomain(): RiceVarietyModel {
    return RiceVarietyModel(
        id = this.id,
        name = this.name,
        description = this.description,
        imageUrl = this.imageUrl,
        createdAt = Instant.parse(this.createdAt).toEpochMilli(),
        updatedAt = Instant.parse(this.updatedAt).toEpochMilli(),
        deletedAt = this.deletedAt?.let { Instant.parse(it).toEpochMilli() })
}

// To map from Room Entity to Domain Model
fun RiceVarietyEntity.toDomain(): RiceVarietyModel {
    return com.example.fieldsync_inventory_app.domain.model.RiceVariety(
        id = this.id,
        name = this.name,
        description = this.description,
        imageUrl = this.imageUrl,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        deletedAt = this.deletedAt
    )
}

// Mapper to convert Domain Model to Request DTO (for PUT/POST)
fun RiceVarietyModel.toRequestDto(): RiceVarietyRequestDto {
    return RiceVarietyRequestDto(
        name = this.name,
        description = this.description
    )
}