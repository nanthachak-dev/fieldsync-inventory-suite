package com.example.fieldsync_inventory_app.data.security

import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.aead.AeadKeyTemplates
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import com.google.crypto.tink.config.TinkConfig
import com.google.crypto.tink.RegistryConfiguration
import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import java.security.KeyStore
import javax.inject.Inject
import androidx.core.content.edit

class KeyManager @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    private val KEYSET_NAME = "master_keyset"
    private val PREF_FILE_NAME = "key_storage_file"
    private val MASTER_KEY_URI = "android-keystore://master_key"
    private val MASTER_KEY_ALIAS = "master_key"

    init {
        TinkConfig.register()
    }

    private val keysetHandle: KeysetHandle = try {
        buildKeysetHandle()
    } catch (e: Exception) {
        Log.e("KeyManager", "Failed to initialize KeysetHandle, attempting recovery", e)
        recoverKeyset()
    }

    private fun buildKeysetHandle(): KeysetHandle {
        return AndroidKeysetManager.Builder()
            .withSharedPref(context, KEYSET_NAME, PREF_FILE_NAME)
            .withKeyTemplate(AeadKeyTemplates.AES256_GCM)
            .withMasterKeyUri(MASTER_KEY_URI)
            .build()
            .keysetHandle
    }

    private fun recoverKeyset(): KeysetHandle {
        return try {
            // 1. Delete the master key from Android Keystore
            val keyStore = KeyStore.getInstance("AndroidKeyStore")
            keyStore.load(null)
            if (keyStore.containsAlias(MASTER_KEY_ALIAS)) {
                keyStore.deleteEntry(MASTER_KEY_ALIAS)
            }

            // 2. Clear the shared preferences where the encrypted keyset is stored
            context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE).edit { clear() }

            // 3. Try building again
            buildKeysetHandle()
        } catch (e: Exception) {
            Log.e("KeyManager", "Recovery failed definitively", e)
            throw e
        }
    }

    val aead: Aead = keysetHandle.getPrimitive(
        RegistryConfiguration.get(),
        Aead::class.java
    )
}