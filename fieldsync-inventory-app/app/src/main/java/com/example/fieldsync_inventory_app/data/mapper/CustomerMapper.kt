package com.example.fieldsync_inventory_app.data.mapper

import com.example.fieldsync_inventory_app.data.local.entity.CustomerEntity
import com.example.fieldsync_inventory_app.data.remote.dto.customer.CustomerRequestDto
import com.example.fieldsync_inventory_app.data.remote.dto.customer.CustomerResponseDto
import com.example.fieldsync_inventory_app.domain.model.Customer
import java.time.Instant

// -- Mapper --

// To map from API response to Room Entity
fun CustomerResponseDto.toEntity(): CustomerEntity {
    return CustomerEntity(
        id = this.id,
        customerTypeId = this.customerType.id,
        customerTypeName = this.customerType.name,
        fullName = this.fullName,
        email = this.email,
        phone = this.phone,
        address = this.address,
        createdAt = Instant.parse(this.createdAt).toEpochMilli(),
        updatedAt = Instant.parse(this.updatedAt).toEpochMilli(),
        deletedAt = this.deletedAt?.let { Instant.parse(it).toEpochMilli() }
    )
}

// To map from API response to Domain Model
fun CustomerResponseDto.toDomain(): Customer {
    return Customer(
        id = this.id,
        customerTypeId = this.customerType.id,
        customerTypeName = this.customerType.name,
        fullName = this.fullName,
        email = this.email,
        phone = this.phone,
        address = this.address,
        createdAt = Instant.parse(this.createdAt).toEpochMilli(),
        updatedAt = Instant.parse(this.updatedAt).toEpochMilli(),
        deletedAt = this.deletedAt?.let { Instant.parse(it).toEpochMilli() }
    )
}

// To map from Room Entity to Domain Model
fun CustomerEntity.toDomain(): Customer {
    return Customer(
        id = this.id,
        customerTypeId = this.customerTypeId,
        customerTypeName = this.customerTypeName,
        fullName = this.fullName,
        email = this.email,
        phone = this.phone,
        address = this.address,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        deletedAt = this.deletedAt
    )
}

// Mapper to convert Domain Model to Request DTO (for PUT/POST)
fun Customer.toRequestDto(): CustomerRequestDto {
    return CustomerRequestDto(
        customerTypeId = this.customerTypeId,
        fullName = this.fullName,
        email = this.email,
        phone = this.phone,
        address = this.address
    )
}