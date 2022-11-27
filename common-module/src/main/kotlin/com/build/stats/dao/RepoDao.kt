package com.build.stats.dao

import com.build.stats.extension.TokenAwareRepositoryExtension
import com.build.stats.model.Repo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository


@Repository
interface RepoDao : JpaRepository<Repo, Long>, TokenAwareRepositoryExtension<Repo> {
    /**
     * Finds entity by provided [token].
     */
    @Query(value = "select e from Repo e where e._token = ?1")
    fun findByToken(token: String): Repo?

}