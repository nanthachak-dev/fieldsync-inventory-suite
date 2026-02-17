package com.example.fieldsync_inventory_app.domain.use_case.data_store.token

import com.example.fieldsync_inventory_app.domain.repository.data_store.TokenRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to get the token as a Flow.
 */
class GetTokenFlowUseCase @Inject constructor(
    private val tokenRepository: TokenRepository
) {
    operator fun invoke(): Flow<String?> {
        return tokenRepository.getTokenFlow()
    }
}