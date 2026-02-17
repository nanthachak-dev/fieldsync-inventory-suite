package com.example.fieldsync_inventory_app

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltAndroidApp
class FieldSyncInventoryApplication: Application() {
    // Suppress Hilt warning >>> warning: [deprecation] applicationContextModule(ApplicationContextModule) in Builder has been deprecated
    @Inject
    @ApplicationContext
    lateinit var context: Context
}
