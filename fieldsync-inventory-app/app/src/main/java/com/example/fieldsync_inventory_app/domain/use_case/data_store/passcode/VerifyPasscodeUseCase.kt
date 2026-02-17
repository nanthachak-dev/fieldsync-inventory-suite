package com.example.fieldsync_inventory_app.domain.use_case.data_store.passcode

import com.example.fieldsync_inventory_app.domain.repository.data_store.PasscodeRepository
import com.example.fieldsync_inventory_app.util.security.SecurityUtils
import com.example.fieldsync_inventory_app.util.security.SecurityUtils.hexStringToByteArray
import kotlinx.coroutines.flow.first
import javax.inject.Inject

data class PasscodeVerificationResult(
    val isCorrect: Boolean,
    val attemptCount: Int,
    val shouldClearPasscode: Boolean
)
class VerifyPasscodeUseCase @Inject constructor(
    private val passcodeRepository: PasscodeRepository
) {
    companion object {
        const val MAX_ATTEMPTS = 4
    }

    suspend operator fun invoke(inputPasscode: String): PasscodeVerificationResult {
        val settings = passcodeRepository.passcodeSettingsFlow.first()

        // Check if passcode exists
        if (settings.passcodeHash.isEmpty() || settings.passcodeSalt.isEmpty()) {
            return PasscodeVerificationResult(
                isCorrect = false,
                attemptCount = 0,
                shouldClearPasscode = false
            )
        }

        // Convert stored salt from hex string to bytes
        val saltBytes = settings.passcodeSalt.hexStringToByteArray()

        // Hash input passcode with the SAME salt
        val inputHash = SecurityUtils.hashPasscode(inputPasscode, saltBytes)

        return if (inputHash == settings.passcodeHash) {
            // Correct passcode - reset attempts
            passcodeRepository.resetWrongAttempts()
            PasscodeVerificationResult(
                isCorrect = true,
                attemptCount = 0,
                shouldClearPasscode = false
            )
        } else {
            // Wrong passcode - increment attempts
            passcodeRepository.incrementWrongAttempts()
            val newCount = passcodeRepository.getWrongAttemptCount()

            // Check if max attempts reached
            val shouldClear = newCount >= MAX_ATTEMPTS
            if (shouldClear) {
                passcodeRepository.clearPasscode()
            }

            PasscodeVerificationResult(
                isCorrect = false,
                attemptCount = newCount,
                shouldClearPasscode = shouldClear
            )
        }
    }
}