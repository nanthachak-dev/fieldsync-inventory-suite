package com.example.fieldsync_inventory_app.domain.use_case.data_store.token

import com.example.fieldsync_inventory_app.domain.repository.data_store.TokenRepository
import javax.inject.Inject

/**
 * Use case to clear the stored token.
 */
class ClearTokenUseCase @Inject constructor(
    private val tokenRepository: TokenRepository
) {
    suspend operator fun invoke() {
        tokenRepository.clearToken()
    }
}