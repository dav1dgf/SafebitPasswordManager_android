package com.example.safebit.exceptions

/**
 * Custom exception class to handle database-related errors.
 *
 * @param message A specific error message that describes the issue.
 * @param cause The underlying reason for the exception (optional).
 */
class DataBaseException(
    message: String = "Database Error",
    cause: Throwable? = null,

) : Exception(message, cause) {

    override fun toString(): String {
        return buildString {
            append("DataBaseException: $message")
            cause?.let { append(", Caused by: ${it.message}") }
        }
    }
}