package com.example.fieldsync_inventory_app.domain.use_case.data_store.token

import java.util.Date

/**
 * Sealed class representing the different states of a JWT token.
 */
sealed class TokenStatus {
    /** No token is stored */
    data object NoToken : TokenStatus()

    /** Token is valid and not expired */
    data class Valid(
        val expiresAt: Date,
        val timeUntilExpiryMs: Long
    ) : TokenStatus()

    /** Token is expired */
    data object Expired : TokenStatus()

    /** Token is invalid or couldn't be decoded */
    data class Invalid(val reason: String) : TokenStatus()
}

// Extension function to check if token is usable
fun TokenStatus.isUsable(): Boolean = this is TokenStatus.Valid

// Extension function to check if token needs refresh (e.g., expires in less than 5 minutes)
fun TokenStatus.needsRefresh(thresholdMs: Long = 5 * 60 * 1000): Boolean {
    return when (this) {
        is TokenStatus.Valid -> timeUntilExpiryMs < thresholdMs
        is TokenStatus.Expired -> true
        else -> false
    }
}