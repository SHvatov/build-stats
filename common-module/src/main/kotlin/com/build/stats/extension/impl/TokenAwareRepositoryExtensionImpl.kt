package com.build.stats.extension.impl

import com.build.stats.extension.TokenAwareRepositoryExtension
import com.build.stats.model.aware.TokenAware
import org.hibernate.exception.ConstraintViolationException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Component
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.transaction.Transactional


@Component
class TokenAwareRepositoryExtensionImpl<E : TokenAware> : TokenAwareRepositoryExtension<E> {
    @set:PersistenceContext
    lateinit var entityManager: EntityManager

    @Transactional
    override fun save(entity: E): E {
        if (entity.token == TokenAware.EMPTY_TOKEN) {
            entity.token = prepareNewToken()
        }

        for (i in 0 until MAX_ATTEMPTS_NUM) {
            try {
                return entityManager.merge(entity)
            } catch (ex: Throwable) {
                if (ex !is DataIntegrityViolationException && ex !is ConstraintViolationException) {
                    throw ex
                }

                entity.token = prepareNewToken()
            }
        }

        throw IllegalStateException("Could not generate a unique token for the entity")
    }

    private companion object {
        fun prepareNewToken(): String = UUID.randomUUID().toString()

        const val MAX_ATTEMPTS_NUM = 10
    }
}