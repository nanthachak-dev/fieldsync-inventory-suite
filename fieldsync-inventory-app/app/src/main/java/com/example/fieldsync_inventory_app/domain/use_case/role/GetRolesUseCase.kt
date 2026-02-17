package com.example.fieldsync_inventory_app.domain.use_case.role

import com.example.fieldsync_inventory_app.domain.model.Role
import com.example.fieldsync_inventory_app.domain.repository.role.RoleRepository
import javax.inject.Inject

class GetRolesUseCase @Inject constructor(
    private val repository: RoleRepository
) {
    suspend operator fun invoke(): List<Role> {
        return repository.getRoles()
    }
}
