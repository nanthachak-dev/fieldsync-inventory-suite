package com.example.fieldsync_inventory_app.domain.use_case.data_store.passcode

import com.example.fieldsync_inventory_app.domain.repository.data_store.PasscodeRepository
import com.example.fieldsync_inventory_app.domain.repository.data_store.PasscodeSettings
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPasscodeSettingsUseCase @Inject constructor(
    private val passcodeRepository: PasscodeRepository
) {
    operator fun invoke(): Flow<PasscodeSettings> {
        return passcodeRepository.passcodeSettingsFlow
    }
}