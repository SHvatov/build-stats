package com.build.stats.extension

import com.build.stats.model.aware.TokenAware


interface TokenAwareRepositoryExtension<E : TokenAware> {
    /**
     * Attempts to save [entity] in the data base. If no [TokenAware.token] is provided,
     * then tries to initialize it using specific generator. This process can fail if executed
     * concurrently, thus it is attempted multiple times.
     */
    fun save(entity: E): E
}