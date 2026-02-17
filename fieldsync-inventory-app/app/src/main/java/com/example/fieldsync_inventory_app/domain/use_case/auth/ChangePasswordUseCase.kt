package com.example.fieldsync_inventory_app.domain.use_case.auth

import com.example.fieldsync_inventory_app.domain.model.ChangePassword
import com.example.fieldsync_inventory_app.domain.repository.auth.AuthRepository
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(changePassword: ChangePassword) {
        repository.changePassword(changePassword)
    }
}
