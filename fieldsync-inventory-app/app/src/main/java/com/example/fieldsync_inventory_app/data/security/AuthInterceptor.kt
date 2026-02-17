package com.example.fieldsync_inventory_app.data.security

import com.example.fieldsync_inventory_app.domain.repository.data_store.TokenRepository
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import kotlinx.coroutines.runBlocking // 🔑 CRITICAL IMPORT 🔑

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenRepository // Hilt injects this
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Retrieve the token securely using runBlocking
        val token = runBlocking {
            // We use the suspending getToken() function to get the one-off value.
            // This blocks the interceptor's thread until the DataStore read completes.
            tokenManager.getToken()
        }

        // Check and build the request (Logic remains the same)
        if (token.isNullOrEmpty()) {
            return chain.proceed(originalRequest)
        }

        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()

        return chain.proceed(newRequest)
    }
}