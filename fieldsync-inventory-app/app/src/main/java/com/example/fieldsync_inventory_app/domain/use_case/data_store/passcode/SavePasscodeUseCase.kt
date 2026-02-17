package com.example.fieldsync_inventory_app.domain.use_case.data_store.passcode

import com.example.fieldsync_inventory_app.domain.repository.data_store.PasscodeRepository
import com.example.fieldsync_inventory_app.util.security.SecurityUtils
import javax.inject.Inject

class SavePasscodeUseCase @Inject constructor(
    private val passcodeRepository: PasscodeRepository
) {
    suspend operator fun invoke(passcode: String) {
        // Generate random salt
        val saltBytes = SecurityUtils.generateSalt()

        // Hash passcode with salt
        val hashBytes = SecurityUtils.hashPasscode(passcode, saltBytes)

        // Convert to hex strings for storage
        val hashHex = hashBytes
        val saltHex = saltBytes.toHexString()

        // Save both hash and salt
        passcodeRepository.savePasscodeHash(hashHex, saltHex)
    }
}