package com.example.fieldsync_inventory_app.domain.use_case.data_store.token

import com.auth0.jwt.JWT
import com.auth0.jwt.exceptions.JWTDecodeException
import com.example.fieldsync_inventory_app.domain.repository.data_store.TokenRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

/**
 * Use case to check if the JWT token is expired.
 *
 * This use case decodes the JWT token and checks its expiration claim.
 * It returns a Flow that emits TokenStatus to allow reactive UI updates.
 */
class CheckTokenExpirationUseCase @Inject constructor(
    private val tokenRepository: TokenRepository
) {
    /**
     * Returns a Flow that emits the current token status.
     * The UI can collect this Flow to reactively update based on token state.
     */
    operator fun invoke(): Flow<TokenStatus> {
        return tokenRepository.getTokenFlow().map { token ->
            when {
                token == null -> TokenStatus.NoToken
                else -> checkTokenExpiration(token)
            }
        }
    }

    /**
     * One-time check for token expiration (suspend function).
     * Useful for making decisions before API calls.
     */
    suspend fun checkOnce(): TokenStatus {
        val token = tokenRepository.getToken()
        return when {
            token == null -> TokenStatus.NoToken
            else -> checkTokenExpiration(token)
        }
    }

    private fun checkTokenExpiration(token: String): TokenStatus {
        return try {
            val decodedJWT = JWT.decode(token)
            val expiresAt = decodedJWT.expiresAt

            when {
                expiresAt == null -> TokenStatus.Invalid("Token has no expiration claim")
                expiresAt.before(Date()) -> TokenStatus.Expired
                else -> {
                    val timeUntilExpiry = expiresAt.time - System.currentTimeMillis()
                    TokenStatus.Valid(expiresAt, timeUntilExpiry)
                }
            }
        } catch (e: JWTDecodeException) {
            TokenStatus.Invalid("Failed to decode token: ${e.message}")
        } catch (e: Exception) {
            TokenStatus.Invalid("Error checking token: ${e.message}")
        }
    }
}