package com.build.stats.service.impl

import com.build.stats.dao.*
import com.build.stats.model.BuildStatistics
import com.build.stats.model.UserToRepoLink
import com.build.stats.service.GitHubIntegrationService
import com.build.stats.service.RepositoryApiService
import com.build.stats.utils.findRequiredById
import com.build.stats.utils.getPage
import com.build.stats.utils.prepareInterval
import com.build.stats.vo.filter.UiFilterData
import com.build.stats.vo.repo.RegisteredRepoData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime


@Service
class RepositoryApiServiceImpl @Autowired constructor(
    private val gitHubIntegrationService: GitHubIntegrationService,
    private val buildStatisticsDao: BuildStatisticsDao,
    private val userToRepoLinkDao: UserToRepoLinkDao,
    private val buildDao: BuildDao,
    private val repoDao: RepoDao,
    private val userDao: UserDao,
) : RepositoryApiService {
    override fun registerRepository(userId: Long, gitHubRepoId: Long) {
        val existingRepo = repoDao.findByGitHubId(gitHubRepoId)
        if (existingRepo != null) {
            userToRepoLinkDao.save(
                UserToRepoLink(
                    repositoryId = existingRepo.id,
                    userId = userId
                )
            )
            return
        }

        val newRepo = gitHubIntegrationService
            .fetchRepositoryData(userId, gitHubRepoId)
            .toRepoEntity()
            .let { repoDao.save(it) }

        val stats = buildStatisticsDao.save(
            BuildStatistics(repositoryId = newRepo.id)
        )

        repoDao.save(newRepo.apply {
            buildStatisticsId = stats.id
        })

        userToRepoLinkDao.save(
            UserToRepoLink(
                repositoryId = newRepo.id,
                userId = userId
            )
        )
    }

    override fun prepareRegisteredUserRepositoriesData(userId: Long, filter: UiFilterData?): List<RegisteredRepoData> {
        val user = userDao.findRequiredById(userId)
        val repositoryIds = userToRepoLinkDao.findAllByUser(user.id)
            .map { it.repositoryId }
            .ifEmpty { return emptyList() } // todo: at some point replace with native query with filter
        val repositories = repoDao.findAllById(repositoryIds)

        val latestBuildByRepoId = repositories.associateBy { it.id }
            .mapValues { (_, repo) ->
                buildDao.findLatestByRepository(repo.id, SINGLE_ELEMENT_REQUEST)
                    .firstOrNull()
            }

        return repositories.filter { repo ->
            if (filter != null) {
                val namePattern = filter.getAttribute<String>(REPO_NAME_PATTERN_ATTR)
                val latestBuildBefore = filter.getAttribute<LocalDateTime>(REPO_LATEST_BUILD_BEFORE)
                val latestBuildAfter = filter.getAttribute<LocalDateTime>(REPO_LATEST_BUILD_AFTER)
                val latestBuild = latestBuildByRepoId[repo.id]

                val isBuildDateFilterPresent = latestBuildAfter != null || latestBuildBefore != null
                if (latestBuild == null && isBuildDateFilterPresent) {
                    return@filter false
                } else if (latestBuild != null && isBuildDateFilterPresent) {
                    val isInDateRange = latestBuild.started in prepareInterval(latestBuildAfter, latestBuildBefore)
                    if (!isInDateRange) {
                        return@filter false
                    }
                }

                return@filter namePattern?.let {
                    repo.name?.contains(namePattern) ?: false
                } ?: true
            } else true
        }.map {
            val latestBuild = latestBuildByRepoId[it.id]
            RegisteredRepoData(
                it.id,
                it.name,
                it.description,
                it.link,
                latestBuild?.status,
                latestBuild?.started,
            )
        }.let {
            if (filter?.isPaged == true) {
                it.getPage(filter.page, filter.pageSize)
            } else it
        }
    }

    private companion object {
        val SINGLE_ELEMENT_REQUEST: Pageable = PageRequest.of(0, 1)

        const val REPO_NAME_PATTERN_ATTR = "namePattern"

        const val REPO_LATEST_BUILD_BEFORE = "latestBuildBefore"

        const val REPO_LATEST_BUILD_AFTER = "latestBuildAfter"
    }
}