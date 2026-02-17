package com.example.fieldsync_inventory_app.domain.use_case.auth

import com.example.fieldsync_inventory_app.data.remote.dto.auth.request.LoginRequest
import com.example.fieldsync_inventory_app.domain.repository.auth.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(loginRequest: LoginRequest) = authRepository.login(loginRequest)
}
