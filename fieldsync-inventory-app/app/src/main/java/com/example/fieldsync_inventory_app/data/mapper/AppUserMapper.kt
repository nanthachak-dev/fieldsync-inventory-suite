package com.example.fieldsync_inventory_app.data.mapper

import com.example.fieldsync_inventory_app.data.remote.dto.app_user.AppUserRequestDto
import com.example.fieldsync_inventory_app.data.remote.dto.app_user.AppUserResponseDto
import com.example.fieldsync_inventory_app.domain.model.AppUser
import java.time.Instant

fun AppUserResponseDto.toDomain(): AppUser {
    return AppUser(
        id = this.id,
        username = this.username,
        password = this.password,
        roles = this.roles,
        isActive = this.isActive,
        createdAt = try { Instant.parse(this.createdAt).toEpochMilli() } catch (e: Exception) { 0L },
        updatedAt = try { Instant.parse(this.updatedAt).toEpochMilli() } catch (e: Exception) { 0L },
        deletedAt = this.deletedAt?.let { try { Instant.parse(it).toEpochMilli() } catch (e: Exception) { null } }
    )
}

fun AppUser.toRequestDto(): AppUserRequestDto {
    return AppUserRequestDto(
        username = this.username,
        password = this.password,
        isActive = this.isActive,
        roles = this.roles
    )
}
