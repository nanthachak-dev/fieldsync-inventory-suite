package com.example.fieldsync_inventory_app.domain.use_case.data_store.passcode

import com.example.fieldsync_inventory_app.domain.repository.data_store.PasscodeRepository
import javax.inject.Inject

class ClearPasscodeUseCase @Inject constructor(
    private val passcodeRepository: PasscodeRepository
) {
    suspend operator fun invoke() {
        passcodeRepository.clearPasscode()
    }
}