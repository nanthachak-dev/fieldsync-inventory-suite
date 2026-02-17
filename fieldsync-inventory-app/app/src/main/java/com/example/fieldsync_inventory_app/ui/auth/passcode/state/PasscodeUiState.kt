package com.example.fieldsync_inventory_app.ui.auth.passcode.state

data class PasscodeUiState(
    val currentPasscode: String? = null,
    val passcode: String? = null,
    val confirmPasscode: String? = null
) {
    val isEmpty: Boolean
        get() = !passcode.isNullOrEmpty()

    val isPasswordMatch: Boolean
        get() = passcode == confirmPasscode

    val isPasswordLengthValid: Boolean
        get() = passcode?.length == 4

    val isPasswordDigit: Boolean
        get() = passcode?.any { it.isDigit() } ?: false

    val isPasswordNotSequential: Boolean
        get() {
            if (passcode.isNullOrEmpty() || passcode.length != 4) return false

            val digits = passcode.map { it.digitToInt() }

            // Check for ascending sequence (e.g., 1234, 5678)
            val isAscending = digits.zipWithNext().all { (a, b) -> b == a + 1 }

            // Check for descending sequence (e.g., 4321, 8765)
            val isDescending = digits.zipWithNext().all { (a, b) -> b == a - 1 }

            return !isAscending && !isDescending
        }

    val isPasswordNotRepeating: Boolean
        get() {
            if (passcode.isNullOrEmpty() || passcode.length != 4) return false

            // Check if all digits are the same (e.g., 1111, 5555)
            return passcode.toSet().size > 1
        }

    val isPasswordValid: Boolean
        get() = isPasswordLengthValid &&
                isPasswordDigit &&
                isPasswordNotSequential &&
                isPasswordNotRepeating
}