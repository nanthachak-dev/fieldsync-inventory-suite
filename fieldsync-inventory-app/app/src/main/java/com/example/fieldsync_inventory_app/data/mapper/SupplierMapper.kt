package com.example.fieldsync_inventory_app.data.mapper

import com.example.fieldsync_inventory_app.data.local.entity.SupplierEntity
import com.example.fieldsync_inventory_app.data.remote.dto.supplier.SupplierRequestDto
import com.example.fieldsync_inventory_app.data.remote.dto.supplier.SupplierResponseDto
import com.example.fieldsync_inventory_app.domain.model.Supplier

fun SupplierResponseDto.toEntity(): SupplierEntity {
    return SupplierEntity(
        id = this.id,
        supplierTypeId = this.supplierType.id,
        supplierTypeName = this.supplierType.name,
        fullName = this.fullName,
        email = this.email,
        phone = this.phone,
        address = this.address,
        description = this.description
    )
}

fun SupplierResponseDto.toDomain(): Supplier {
    return Supplier(
        id = this.id,
        supplierTypeId = this.supplierType.id,
        supplierTypeName = this.supplierType.name,
        fullName = this.fullName,
        email = this.email,
        phone = this.phone,
        address = this.address,
        description = this.description
    )
}

fun SupplierEntity.toDomain(): Supplier {
    return Supplier(
        id = this.id,
        supplierTypeId = this.supplierTypeId,
        supplierTypeName = this.supplierTypeName,
        fullName = this.fullName,
        email = this.email,
        phone = this.phone,
        address = this.address,
        description = this.description
    )
}

fun Supplier.toRequestDto(): SupplierRequestDto {
    return SupplierRequestDto(
        supplierTypeId = this.supplierTypeId,
        fullName = this.fullName,
        email = this.email,
        phone = this.phone,
        address = this.address,
        description = this.description
    )
}
