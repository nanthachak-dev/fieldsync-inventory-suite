package com.example.fieldsync_inventory_app.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.example.fieldsync_inventory_app.data.data_store.AppSettingsDataStore
import com.example.fieldsync_inventory_app.data.data_store.PasscodeDataStore
import com.example.fieldsync_inventory_app.data.data_store.TokenDataStore
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    private const val TOKEN_PREFS_FILE = "token_storage"
    private const val PASSCODE_PREFS_FILE = "passcode_storage"
    private const val APP_SETTINGS_PREFS_FILE = "app_settings"

    @TokenDataStore
    @Provides
    @Singleton
    fun provideTokenDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile(TOKEN_PREFS_FILE) }
        )
    }

    @PasscodeDataStore // ✅ New qualifier
    @Provides
    @Singleton
    fun providePasscodeDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile(PASSCODE_PREFS_FILE) }
        )
    }

    @AppSettingsDataStore
    @Provides
    @Singleton
    fun provideAppSettingsDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile(APP_SETTINGS_PREFS_FILE) }
        )
    }
}