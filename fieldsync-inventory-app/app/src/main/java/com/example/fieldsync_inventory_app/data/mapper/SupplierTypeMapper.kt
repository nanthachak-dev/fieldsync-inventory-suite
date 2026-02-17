package com.example.fieldsync_inventory_app.data.mapper

import com.example.fieldsync_inventory_app.data.local.entity.SupplierTypeEntity
import com.example.fieldsync_inventory_app.data.remote.dto.supplier_type.SupplierTypeRequestDto
import com.example.fieldsync_inventory_app.data.remote.dto.supplier_type.SupplierTypeResponseDto
import com.example.fieldsync_inventory_app.domain.model.SupplierType

// To map from API response to Room Entity
fun SupplierTypeResponseDto.toEntity(): SupplierTypeEntity {
    return SupplierTypeEntity(
        id = this.id,
        name = this.name,
        description = this.description
    )
}

// To map from API response to Domain Model
fun SupplierTypeResponseDto.toDomain(): SupplierType {
    return SupplierType(
        id = this.id,
        name = this.name,
        description = this.description
    )
}

// To map from Room Entity to Domain Model
fun SupplierTypeEntity.toDomain(): SupplierType {
    return SupplierType(
        id = this.id,
        name = this.name,
        description = this.description
    )
}

// Mapper to convert Domain Model to Request DTO (for PUT/POST)
fun SupplierType.toRequestDto(): SupplierTypeRequestDto {
    return SupplierTypeRequestDto(
        name = this.name,
        description = this.description
    )
}
