package com.example.fieldsync_inventory_app.domain.use_case.app_user

import com.example.fieldsync_inventory_app.domain.model.AppUser
import com.example.fieldsync_inventory_app.domain.repository.app_user.AppUserRepository
import javax.inject.Inject

class GetAppUsersUseCase @Inject constructor(
    private val repository: AppUserRepository
) {
    suspend operator fun invoke(): List<AppUser> {
        return repository.getAppUsers()
    }
}
