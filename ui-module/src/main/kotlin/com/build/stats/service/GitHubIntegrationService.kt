package com.build.stats.service

import com.build.stats.vo.repo.RemoteRepoData


interface GitHubIntegrationService {
    /**
     * Based on the provided [userId] fetches all the data about
     */
    fun fetchUserRepositories(userId: Long): List<RemoteRepoData>

    /**
     * Using the credentials of the user with [userId] fetches full data
     * about repository with [gitHubRepoId] which is needed in order to register
     * that repository in the system.
     */
    fun fetchRepositoryData(userId: Long, gitHubRepoId: Long): RemoteRepoData
}