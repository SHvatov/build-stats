package com.build.stats.dao

import com.build.stats.extension.TokenAwareRepositoryExtension
import com.build.stats.model.Build
import com.build.stats.model.BuildStatus
import org.springframework.data.domain.Pageable
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

    /**
     * Finds all the entities with [BuildStatus.IN_PROGRESS] that has exceeded
     * the configured timeout.
     */
    @Query(nativeQuery = true, value = ACTIVE_WITH_TIMEOUT_QUERY)
    fun findActiveWithTimeout(): List<Long>

    /**
     * Finds all the [Build] entities with [status].
     */
    @Query(value = "select e from Build e where e._status = ?1")
    fun findAllByStatus(status: BuildStatus): List<Build>

    /**
     * Finds a [page] of [Build] entities by the [repositoryId].
     */
    @Query(value = "select e from Build e where e._repositoryId = ?1")
    fun findByRepository(repositoryId: Long, page: Pageable): List<Build>

    /**
     * Finds all [Build] entities by the [repositoryId].
     */
    @Query(value = "select e from Build e where e._repositoryId = ?1")
    fun findAllByRepository(repositoryId: Long): List<Build>

    /**
     * Finds a [page] of [Build] entities by the [repositoryId].
     */
    @Query(value = "select e from Build e where e._repositoryId = ?1 order by e._started desc")
    fun findLatestByRepository(repositoryId: Long, page: Pageable): List<Build>

    private companion object {
        const val ACTIVE_WITH_TIMEOUT_QUERY =
            "select gb.id\n" +
                    "from gh_build gb\n" +
                    "         join gh_repository gr on gb.repo_id = gr.id\n" +
                    "where gb.status = 'IN_PROGRESS'\n" +
                    "  and EXTRACT(MILLISECONDS FROM (current_timestamp - gb.started)) >=\n" +
                    "      (gr.config::json ->> 'buildTimeout')::numeric;"
    }
}