package com.example.fieldsync_inventory_app.domain.repository.data_store

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.fieldsync_inventory_app.data.data_store.PreferencesKeys
import com.example.fieldsync_inventory_app.data.data_store.TokenDataStore
import com.example.fieldsync_inventory_app.data.security.KeyManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.nio.charset.StandardCharsets
import javax.inject.Inject

class TokenRepository @Inject constructor(
    // Use the qualifier to specify which DataStore<Preferences> to use
    @param:TokenDataStore private val tokenDataStore: DataStore<Preferences>,
    private val keyManager: KeyManager
) {
    private val ASSOCIATED_DATA = "token_ad".toByteArray(StandardCharsets.UTF_8)

    /**
     * Saves the token by encrypting it first. (Remains a suspending function)
     */
    suspend fun saveToken(token: String) {
        val plaintextBytes = token.toByteArray(StandardCharsets.UTF_8)

        // Encrypt the plaintext token using the Tink Aead primitive
        val encryptedBytes = keyManager.aead.encrypt(plaintextBytes, ASSOCIATED_DATA)
        val encryptedToken = encryptedBytes.toHexString()

        tokenDataStore.edit { preferences ->
            preferences[PreferencesKeys.ACCESS_TOKEN] = encryptedToken
        }
    }

    /**
     * Exposes the JWT token as a Flow of nullable strings.
     * This allows the ViewModel to collect changes reactively.
     */
    fun getTokenFlow(): Flow<String?> {
        return tokenDataStore.data.map { preferences ->
            val encryptedToken = preferences[PreferencesKeys.ACCESS_TOKEN]

            if (encryptedToken.isNullOrEmpty()) return@map null

            // Decryption logic inside the map operator
            return@map try {
                val ciphertextBytes = encryptedToken.hexStringToByteArray()
                val plaintextBytes = keyManager.aead.decrypt(ciphertextBytes,
                    ASSOCIATED_DATA)
                String(plaintextBytes, StandardCharsets.UTF_8)
            } catch (e: Exception) {
                // IMPORTANT: Log and handle potential corruption.
                // Since this is a Flow, clearing the token must be handled
                // outside or by using an action-based Flow (like Channel/SharedFlow).
                // For simplicity here, we return null.
                null
            }
        }
    }

    /**
     * Synchronous read of the token for one-off use (e.g., OkHttp Interceptors).
     * This uses the .first() operator to immediately suspend and return the current token value.
     *
     * NOTE: This should ONLY be used in contexts where a reactive Flow is not possible,
     * such as the synchronous intercept() method of OkHttp.
     */
    suspend fun getToken(): String? {
        // We reuse the existing decryption logic defined in getTokenFlow()
        // but terminate the Flow immediately to get the current value.
        return getTokenFlow().first()
    }

    /**
     * The old 'getToken' that blocked has been replaced by the Flow above.
     * If you still need a one-off synchronous read (not recommended in Compose),
     * you can add the `.first()` back here, but for reactive state, use the Flow.
     * * NOTE: We are keeping the clearToken suspending function.
     */
    suspend fun clearToken() {
        tokenDataStore.edit { it.remove(PreferencesKeys.ACCESS_TOKEN) }
    }

    // Helper extensions (unchanged)
    private fun ByteArray.toHexString(): String = joinToString("") { "%02x".format(it) }
    private fun String.hexStringToByteArray(): ByteArray = chunked(2)
        .map { it.toInt(16).toByte() }
        .toByteArray()
}