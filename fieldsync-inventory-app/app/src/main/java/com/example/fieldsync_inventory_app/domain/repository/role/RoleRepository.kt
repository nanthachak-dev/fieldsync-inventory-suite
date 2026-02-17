package com.example.fieldsync_inventory_app.domain.repository.role

import com.example.fieldsync_inventory_app.domain.model.Role

interface RoleRepository {
    suspend fun getRoles(): List<Role>
    suspend fun getRoleById(id: Int): Role
    suspend fun saveRole(role: Role): Role
    suspend fun deleteRole(id: Int)
}
