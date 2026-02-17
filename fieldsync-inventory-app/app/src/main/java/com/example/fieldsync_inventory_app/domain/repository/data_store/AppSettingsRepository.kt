package com.example.fieldsync_inventory_app.domain.repository.data_store

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.fieldsync_inventory_app.data.data_store.AppSettingsDataStore
import com.example.fieldsync_inventory_app.data.data_store.PreferencesKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppSettingsRepository @Inject constructor(
    @param:AppSettingsDataStore private val dataStore: DataStore<Preferences>
) {
    /**
     * Exposes whether it's the first launch as a Flow.
     */
    fun isFirstLaunchFlow(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.IS_FIRST_LAUNCH] ?: true
        }
    }

    /**
     * Get the current value of isFirstLaunch.
     */
    suspend fun isFirstLaunch(): Boolean {
        return isFirstLaunchFlow().first()
    }

    /**
     * Updates the first launch flag.
     */
    suspend fun setFirstLaunch(isFirstLaunch: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_FIRST_LAUNCH] = isFirstLaunch
        }
    }
}
