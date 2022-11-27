package com.build.stats.dao

import com.build.stats.extension.TokenAwareRepositoryExtension
import com.build.stats.model.Build
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface BuildDao : JpaRepository<Build, Long>, TokenAwareRepositoryExtension<Build> {
    /**
     * Finds entity by provided [token].
     */
    @Query(value = "select e from Build e where e._token = ?1")
    fun findByToken(token: String): Build?
}