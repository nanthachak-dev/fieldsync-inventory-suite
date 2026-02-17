package com.example.fieldsync_inventory_app.data.data_store

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    // Token keys (existing)
    val ACCESS_TOKEN = stringPreferencesKey("access_token")

    // Passcode keys (new)
    val PASSCODE_HASH = stringPreferencesKey("passcode_hash")
    val PASSCODE_SALT = stringPreferencesKey("passcode_salt")
    val WRONG_ATTEMPT_COUNT = intPreferencesKey("wrong_attempt_count")

    // App settings keys
    val IS_FIRST_LAUNCH = booleanPreferencesKey("is_first_launch")
}