package com.example.fieldsync_inventory_app.data.mapper

import com.example.fieldsync_inventory_app.data.remote.dto.role.RoleRequestDto
import com.example.fieldsync_inventory_app.data.remote.dto.role.RoleResponseDto
import com.example.fieldsync_inventory_app.domain.model.Role
import java.time.Instant

fun RoleResponseDto.toDomain(): Role {
    return Role(
        id = this.id,
        name = this.name,
        description = this.description,
        createdAt = try { Instant.parse(this.createdAt).toEpochMilli() } catch (e: Exception) { 0L },
        updatedAt = try { Instant.parse(this.updatedAt).toEpochMilli() } catch (e: Exception) { 0L },
        deletedAt = this.deletedAt?.let { try { Instant.parse(it).toEpochMilli() } catch (e: Exception) { null } }
    )
}

fun Role.toRequestDto(): RoleRequestDto {
    return RoleRequestDto(
        name = this.name,
        description = this.description
    )
}
