package com.build.stats.utils

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull


/**
 * Returns this value or value generated by [defaultValueProvider] if this is null.
 */
inline fun <T> T?.orElse(defaultValueProvider: () -> T): T {
    return this ?: defaultValueProvider()
}

/**
 * Returns this or throw exception if this is null. [messageProvider]
 * can be used for specific exception message.
 */
fun <T> T?.retrieveRequired(messageProvider: (() -> String)? = null): T {
    return this ?: throw IllegalStateException(
        messageProvider?.invoke() ?: DEFAULT_ENTITY_IS_ABSENT_MSG
    )
}

/**
 * Retrieves required entity of type [T] with [id] or throws corresponding exception.
 */
inline fun <reified T, ID> JpaRepository<T, ID>.findRequiredById(id: ID): T {
    return findByIdOrNull(id)
        .retrieveRequired {
            "Can't find required entity of" +
                    " type ${T::class.simpleName} with id [$id]"
        }
}

private const val DEFAULT_ENTITY_IS_ABSENT_MSG = "Entity is marked as required, but null"