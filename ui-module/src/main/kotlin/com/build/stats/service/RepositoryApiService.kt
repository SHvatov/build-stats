package com.build.stats.service

import com.build.stats.vo.filter.UiFilterData
import com.build.stats.vo.repo.RegisteredRepoData
import org.springframework.transaction.annotation.Transactional


interface RepositoryApiService {
    /**
     * Registers github repository with [gitHubRepoId] using credentials
     *  of the user with [userId].
     */
    @Transactional
    fun registerRepository(userId: Long, gitHubRepoId: Long)

    /**
     * Bases on the provided [userId] and [filter] finds information about the repositories,
     * that are registered in the system and returns a list of VO objects.
     */
    fun prepareRegisteredUserRepositoriesData(userId: Long, filter: UiFilterData?): List<RegisteredRepoData>
}