package com.example.fieldsync_inventory_app.domain.use_case.data_store.token

import com.example.fieldsync_inventory_app.domain.repository.data_store.TokenRepository
import javax.inject.Inject

/**
 * Use case to save a JWT token.
 */
class SaveTokenUseCase @Inject constructor(
    private val tokenRepository: TokenRepository
) {
    suspend operator fun invoke(token: String) {
        tokenRepository.saveToken(token)
    }
}