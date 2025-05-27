package com.pnj.saku_planner.core.util

// Required field validation
fun <T> validateRequired(value: T?, message: String = "This field is required"): String? {
    return when (value) {
        null -> message
        is String -> if (value.isBlank()) message else null
        else -> null
    }
}

// Positive number validation (> 0)
fun validatePositiveNumber(
    value: Long?,
    message: String = "Value must be greater than 0"
): String? {
    return if (value == null || value <= 0.0) message else null
}

// Non-negative number validation (>= 0)
fun validateNonNegativeNumber(
    value: Long?,
    message: String = "Value must be zero or greater"
): String? {
    return if (value == null || value < 0.0) message else null
}

// Validate string minimum length
fun validateMinLength(value: String?, minLength: Int, message: String? = null): String? {
    val error = message ?: "Minimum length is $minLength"
    return if (value == null || value.length < minLength) error else null
}

// Validate string maximum length
fun validateMaxLength(value: String?, maxLength: Int, message: String? = null): String? {
    val error = message ?: "Maximum length is $maxLength"
    return if (value != null && value.length > maxLength) error else null
}

// Validate number range (inclusive)
fun validateNumberRange(
    value: Long?,
    min: Long,
    max: Long,
    message: String? = null
): String? {
    val error = message ?: "Value must be between $min and $max"
    return if (value == null || value < min || value > max) error else null
}

// Validate if value matches regex pattern
fun validatePattern(value: String?, pattern: Regex, message: String): String? {
    return if (value == null || !pattern.matches(value)) message else null
}

// Validate email format (basic)
fun validateEmail(value: String?, message: String = "Invalid email format"): String? {
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$".toRegex()
    return validatePattern(value, emailRegex, message)
}

// Validate exact string length
fun validateExactLength(value: String?, length: Int, message: String? = null): String? {
    val error = message ?: "Length must be exactly $length characters"
    return if (value == null || value.length != length) error else null
}

// Custom predicate validation
fun <T> validate(value: T, predicate: (T) -> Boolean, message: String): String? {
    return if (!predicate(value)) message else null
}
