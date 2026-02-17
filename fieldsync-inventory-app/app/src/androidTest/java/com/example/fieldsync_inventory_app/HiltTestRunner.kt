package com.example.fieldsync_inventory_app

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

/**
 * A custom test runner for Hilt.
 * It's required to use HiltTestApplication for tests.
 */
class HiltTestRunner : AndroidJUnitRunner() {
    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(cl,
            HiltTestApplication::class.java.name, context)
    }
}