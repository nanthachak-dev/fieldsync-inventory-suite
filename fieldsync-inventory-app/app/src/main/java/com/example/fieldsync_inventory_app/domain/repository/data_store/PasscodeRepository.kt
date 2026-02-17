package com.example.fieldsync_inventory_app.domain.repository.data_store

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.fieldsync_inventory_app.data.data_store.PasscodeDataStore
import com.example.fieldsync_inventory_app.data.data_store.PreferencesKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PasscodeRepository @Inject constructor(
    @param:PasscodeDataStore private val dataStore: DataStore<Preferences>
) {

    val passcodeSettingsFlow: Flow<PasscodeSettings> = dataStore.data.map { prefs ->
        PasscodeSettings(
            passcodeHash = prefs[PreferencesKeys.PASSCODE_HASH] ?: "",
            passcodeSalt = prefs[PreferencesKeys.PASSCODE_SALT] ?: "",
            wrongAttemptCount = prefs[PreferencesKeys.WRONG_ATTEMPT_COUNT] ?: 0
        )
    }

    suspend fun savePasscodeHash(hash: String, salt: String) {
        dataStore.edit { prefs ->
            prefs[PreferencesKeys.PASSCODE_HASH] = hash
            prefs[PreferencesKeys.PASSCODE_SALT] = salt
            prefs[PreferencesKeys.WRONG_ATTEMPT_COUNT] = 0
        }
    }

    suspend fun incrementWrongAttempts() {
        dataStore.edit { prefs ->
            val current = prefs[PreferencesKeys.WRONG_ATTEMPT_COUNT] ?: 0
            prefs[PreferencesKeys.WRONG_ATTEMPT_COUNT] = current + 1
        }
    }

    suspend fun resetWrongAttempts() {
        dataStore.edit { prefs ->
            prefs[PreferencesKeys.WRONG_ATTEMPT_COUNT] = 0
        }
    }

    suspend fun clearPasscode() {
        dataStore.edit { prefs ->
            prefs.remove(PreferencesKeys.PASSCODE_HASH)
            prefs.remove(PreferencesKeys.PASSCODE_SALT)
            prefs.remove(PreferencesKeys.WRONG_ATTEMPT_COUNT)
        }
    }

    // New method to get current attempt count
    suspend fun getWrongAttemptCount(): Int {
        return dataStore.data.first()[PreferencesKeys.WRONG_ATTEMPT_COUNT] ?: 0
    }

    suspend fun hasPasscode(): Boolean {
        var hasIt = false
        dataStore.edit { prefs ->
            hasIt = prefs[PreferencesKeys.PASSCODE_HASH]?.isNotEmpty() == true
        }
        return hasIt
    }
}