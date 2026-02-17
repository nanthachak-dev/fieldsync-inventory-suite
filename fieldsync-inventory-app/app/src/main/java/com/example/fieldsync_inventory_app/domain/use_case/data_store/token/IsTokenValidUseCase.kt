package com.example.fieldsync_inventory_app.domain.use_case.data_store.token

import javax.inject.Inject

/**
 * Use case to check if a valid token exists.
 * Returns true if token exists and is valid (not expired).
 */
class IsTokenValidUseCase @Inject constructor(
    private val checkTokenExpirationUseCase: CheckTokenExpirationUseCase
) {
    suspend operator fun invoke(): Boolean {
        return checkTokenExpirationUseCase.checkOnce().isUsable()
    }
}
