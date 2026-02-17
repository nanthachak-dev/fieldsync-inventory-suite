package com.example.fieldsync_inventory_app.domain.repository.data_store

data class PasscodeSettings(
    val passcodeHash: String = "",
    val passcodeSalt: String = "",
    val wrongAttemptCount: Int = 0
)