package com.build.stats.service.impl

import com.build.stats.dao.UserDao
import com.build.stats.service.GitHubIntegrationService
import com.build.stats.utils.findRequiredById
import com.build.stats.vo.repo.RemoteRepoData
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service

/**
 * todo: at some point add checks of the status and some validation
 * @author shvatov
 */
@Service
class GitHubIntegrationServiceImpl @Autowired constructor(
    restTemplateBuilder: RestTemplateBuilder,
    private val objectMapper: ObjectMapper,
    private val userDao: UserDao,
) : GitHubIntegrationService {
    private val restTemplate = restTemplateBuilder.build()

    override fun fetchUserRepositories(userId: Long): List<RemoteRepoData> {
        val user = userDao.findRequiredById(userId)
        return restTemplate.exchange(
            USER_REPO_LIST_URL.format(user.username),
            HttpMethod.GET,
            HttpEntity<Any?>(null, HttpHeaders().apply {
                set(AUTHORIZATION_HEADER, "token ${user.token}")
            }),
            Any::class.java
        ).let { response ->
            @Suppress("unchecked_cast")
            val repos = response.body as? List<Map<String, Any?>>
                ?: throw IllegalStateException("Body of unexpected type!")
            repos.map {
                objectMapper.convertValue(it, RemoteRepoData::class.java)
            }
        }
    }

    override fun fetchRepositoryData(userId: Long, gitHubRepoId: Long): RemoteRepoData {
        val user = userDao.findRequiredById(userId)
        return restTemplate.exchange(
            REPO_URL.format(gitHubRepoId),
            HttpMethod.GET,
            HttpEntity<Any?>(null, HttpHeaders().apply {
                set(AUTHORIZATION_HEADER, "token ${user.token}")
            }),
            Any::class.java
        ).let { response ->
            @Suppress("unchecked_cast")
            val repo = response.body as? Map<String, Any?>
                ?: throw IllegalStateException("Body of unexpected type!")
            objectMapper.convertValue(repo, RemoteRepoData::class.java)
        }
    }

    private companion object {
        const val USER_REPO_LIST_URL = "https://api.github.com/users/%s/repos"

        const val REPO_URL = "https://api.github.com/repositories/%s"

        const val AUTHORIZATION_HEADER = "Authorization"
    }
}