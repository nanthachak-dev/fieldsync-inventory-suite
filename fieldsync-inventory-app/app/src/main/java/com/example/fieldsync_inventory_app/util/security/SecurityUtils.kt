package com.example.fieldsync_inventory_app.util.security

import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

object SecurityUtils {
    private const val PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA256"
    private const val ITERATIONS = 100_000
    private const val KEY_LENGTH = 256
    private const val SALT_LENGTH = 16

    /**
     * Hashes a passcode using PBKDF2 with salt.
     * More secure than plain SHA-256 for passwords.
     */
    fun hashPasscode(passcode: String, salt: ByteArray): String {
        val spec = PBEKeySpec(
            passcode.toCharArray(),
            salt,
            ITERATIONS,
            KEY_LENGTH
        )
        val factory = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM)
        val hash = factory.generateSecret(spec).encoded
        return hash.joinToString("") { "%02x".format(it) }
    }

    /**
     * Generates a random salt for password hashing.
     * @return Random salt bytes
     */
    fun generateSalt(): ByteArray {
        val random = SecureRandom()
        val salt = ByteArray(SALT_LENGTH)
        random.nextBytes(salt)
        return salt
    }

    /**
     * Converts ByteArray to hexadecimal string for storage.
     */
    fun ByteArray.toHexString(): String {
        return joinToString("") { "%02x".format(it) }
    }

    /**
     * Converts hexadecimal string back to ByteArray.
     */
    fun String.hexStringToByteArray(): ByteArray {
        return chunked(2)
            .map { it.toInt(16).toByte() }
            .toByteArray()
    }
}