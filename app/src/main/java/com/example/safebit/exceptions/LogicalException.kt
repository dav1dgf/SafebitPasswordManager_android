package com.example.safebit.exceptions

/**
 * Custom exception class to handle logical errors within the application.
 *
 * @param message A specific error message that describes the logical issue.
 * @param cause The underlying reason for the exception (optional).
 * @param errorCode An optional error code to help identify specific logical errors.
 */
class LogicalException(
    message: String = "Logical Error",
    cause: Throwable? = null,
) : Exception(message, cause) {

    override fun toString(): String {
        return buildString {
            append("LogicalException: $message")
            cause?.let { append(", Caused by: ${it.message}") }
        }
    }
}
