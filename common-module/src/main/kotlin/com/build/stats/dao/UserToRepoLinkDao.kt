package com.build.stats.dao

import com.build.stats.model.UserToRepoLink
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository


@Repository
interface UserToRepoLinkDao : JpaRepository<UserToRepoLink, Long> {
    /**
     * Finds all [UserToRepoLinkDao] entities by [userId].
     */
    @Query(value = "select e from UserToRepoLink e where e._userId = ?1")
    fun findAllByUser(userId: Long): List<UserToRepoLink>

    /**
     * Finds all [UserToRepoLinkDao] entities by [repoId].
     */
    @Query(value = "select e from UserToRepoLink e where e._repositoryId = ?1")
    fun findAllByRepo(repoId: Long): List<UserToRepoLink>

    /**
     * Finds [UserToRepoLinkDao] entity by [userId] and [repoId].
     */
    @Query(value = "select e from UserToRepoLink e where e._userId = ?1 and e._repositoryId = ?2")
    fun findAllByUserAndRepo(userId: Long, repoId: Long): UserToRepoLink?
}