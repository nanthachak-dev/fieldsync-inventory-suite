package com.example.fieldsync_inventory_app.data.data_store

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class TokenDataStore()

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PasscodeDataStore

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AppSettingsDataStore
