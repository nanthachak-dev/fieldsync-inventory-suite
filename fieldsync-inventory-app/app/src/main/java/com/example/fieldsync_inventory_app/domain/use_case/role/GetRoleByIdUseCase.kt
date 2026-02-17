package com.example.fieldsync_inventory_app.domain.use_case.role

import com.example.fieldsync_inventory_app.domain.model.Role
import com.example.fieldsync_inventory_app.domain.repository.role.RoleRepository
import javax.inject.Inject

class GetRoleByIdUseCase @Inject constructor(
    private val repository: RoleRepository
) {
    suspend operator fun invoke(id: Int): Role {
        return repository.getRoleById(id)
    }
}
