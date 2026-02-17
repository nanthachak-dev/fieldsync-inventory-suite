package com.example.fieldsync_inventory_app.data.mapper

import com.example.fieldsync_inventory_app.data.local.entity.SeedBatchEntity
import com.example.fieldsync_inventory_app.data.remote.dto.seed_batch.SeedBatchRequestDto
import com.example.fieldsync_inventory_app.data.remote.dto.seed_batch.SeedBatchResponseDto
import com.example.fieldsync_inventory_app.domain.model.SeedBatch
import java.time.Instant

// To map from API response to Room Entity
fun SeedBatchResponseDto.toEntity(): SeedBatchEntity {
    return SeedBatchEntity(
        id = this.id,
        varietyId = this.variety.id,
        varietyName = this.variety.name,
        generationId = this.generation.id,
        generationName = this.generation.name,
        seasonId = this.season.id,
        seasonName = this.season.name,
        year = this.year,
        grading = this.grading,
        germination = this.germination,
        description = this.description,
        createdAt = Instant.parse(this.createdAt).toEpochMilli(),
        updatedAt = Instant.parse(this.updatedAt).toEpochMilli(),
        deletedAt = this.deletedAt?.let { Instant.parse(it).toEpochMilli() }
    )
}

// To map from API response to Domain Model
fun SeedBatchResponseDto.toDomain(): SeedBatch {
    return SeedBatch(
        id = this.id,
        varietyId = this.variety.id,
        varietyName = this.variety.name,
        generationId = this.generation.id,
        generationName = this.generation.name,
        seasonId = this.season.id,
        seasonName = this.season.name,
        year = this.year,
        grading = this.grading,
        germination = this.germination,
        description = this.description,
        createdAt = Instant.parse(this.createdAt).toEpochMilli(),
        updatedAt = Instant.parse(this.updatedAt).toEpochMilli(),
        deletedAt = this.deletedAt?.let { Instant.parse(it).toEpochMilli() }
    )
}

// To map from Room Entity to Domain Model
fun SeedBatchEntity.toDomain(): SeedBatch {
    return SeedBatch(
        id = this.id,
        varietyId = this.varietyId,
        varietyName = this.varietyName,
        generationId = this.generationId,
        generationName = this.generationName,
        seasonId = this.seasonId,
        seasonName = this.seasonName,
        year = this.year,
        grading = this.grading,
        germination = this.germination,
        description = this.description,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        deletedAt = this.deletedAt
    )
}

// Mapper to convert Domain Model to Request DTO (for PUT/POST)
fun SeedBatch.toRequestDto(): SeedBatchRequestDto {
    return SeedBatchRequestDto(
        varietyId = this.varietyId,
        generationId = this.generationId,
        seasonId = this.seasonId,
        year = this.year,
        grading = this.grading,
        germination = this.germination,
        description = this.description
    )
}