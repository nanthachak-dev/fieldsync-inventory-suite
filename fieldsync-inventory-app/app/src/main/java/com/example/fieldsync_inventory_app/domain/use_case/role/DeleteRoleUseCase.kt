package com.example.fieldsync_inventory_app.domain.use_case.role

import com.example.fieldsync_inventory_app.domain.repository.role.RoleRepository
import javax.inject.Inject

class DeleteRoleUseCase @Inject constructor(
    private val repository: RoleRepository
) {
    suspend operator fun invoke(id: Int) {
        repository.deleteRole(id)
    }
}
