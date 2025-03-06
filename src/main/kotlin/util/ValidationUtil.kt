package com.northshore.util


import jakarta.validation.Validation
import jakarta.validation.Validator
import kotlin.reflect.KClass

object ValidationUtil {
    private val validator: Validator = Validation.buildDefaultValidatorFactory().validator

    fun <T : Any> validate(obj: T, clazz: KClass<T>): Map<String, String> {
        val violations = validator.validate(obj)
        val errors = mutableMapOf<String, String>()

        for (violation in violations) {
            errors[violation.propertyPath.toString()] = violation.message
        }

        return errors
    }
}