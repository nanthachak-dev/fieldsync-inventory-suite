package com.example.fieldsync_inventory_app.domain.use_case.auth

import android.util.Log
import com.auth0.jwt.JWT
import com.auth0.jwt.exceptions.JWTDecodeException
import com.example.fieldsync_inventory_app.domain.use_case.data_store.token.GetTokenUseCase
import javax.inject.Inject

class GetUserIdUseCase @Inject constructor(
    private val getTokenUseCase: GetTokenUseCase
) {
    suspend operator fun invoke(): Int {
        val token = getTokenUseCase()
        if (token != null) {
            try {
                val decodedJWT = JWT.decode(token)
                return decodedJWT.getClaim("userId").asInt() ?: 0
            } catch (e: JWTDecodeException) {
                Log.e("GetUserIdUseCase", "Failed to decode token", e)
            }
        }
        return 0
    }
}
