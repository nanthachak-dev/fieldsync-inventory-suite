package com.example.fieldsync_inventory_app.domain.repository.role

import com.example.fieldsync_inventory_app.data.mapper.toDomain
import com.example.fieldsync_inventory_app.data.mapper.toRequestDto
import com.example.fieldsync_inventory_app.data.remote.api.RoleApi
import com.example.fieldsync_inventory_app.domain.model.Role
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoleRepositoryImpl @Inject constructor(
    private val api: RoleApi
) : RoleRepository {

    override suspend fun getRoles(): List<Role> {
        return api.getRoles().map { it.toDomain() }
    }

    override suspend fun getRoleById(id: Int): Role {
        return api.getRoleById(id).toDomain()
    }

    override suspend fun saveRole(role: Role): Role {
        return if (role.id == 0) {
            api.createRole(role.toRequestDto()).toDomain()
        } else {
            api.updateRole(role.id, role.toRequestDto()).toDomain()
        }
    }

    override suspend fun deleteRole(id: Int) {
        api.deleteRole(id)
    }
}
