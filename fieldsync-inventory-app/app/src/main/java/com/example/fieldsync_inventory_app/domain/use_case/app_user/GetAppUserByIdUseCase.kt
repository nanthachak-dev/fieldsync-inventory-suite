package com.example.fieldsync_inventory_app.domain.use_case.app_user

import com.example.fieldsync_inventory_app.domain.model.AppUser
import com.example.fieldsync_inventory_app.domain.repository.app_user.AppUserRepository
import javax.inject.Inject

class GetAppUserByIdUseCase @Inject constructor(
    private val repository: AppUserRepository
) {
    suspend operator fun invoke(id: Int): AppUser {
        return repository.getAppUserById(id)
    }
}
