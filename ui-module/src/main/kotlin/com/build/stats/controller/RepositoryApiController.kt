package com.build.stats.controller

import com.build.stats.service.GitHubIntegrationService
import com.build.stats.service.RepositoryApiService
import com.build.stats.vo.filter.UiFilterData
import com.build.stats.vo.repo.RegisteredRepoData
import com.build.stats.vo.repo.RemoteRepoData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/repo/api")
class RepositoryApiController @Autowired constructor(
    private val gitHubIntegrationService: GitHubIntegrationService,
    private val repositoryApiService: RepositoryApiService,
) {
    @PostMapping("/list/registered")
    fun fetchRegisteredRepositories(
        @RequestParam("userId") userId: Long,
        @RequestBody(required = false) filter: UiFilterData?
    ): ResponseEntity<List<RegisteredRepoData>> {
        val data = repositoryApiService.prepareRegisteredUserRepositoriesData(userId, filter)
        return ResponseEntity.ok(data)
    }

    @GetMapping("/list/github")
    fun fetchGitHubRepositories(@RequestParam("userId") userId: Long): ResponseEntity<List<RemoteRepoData>> {
        val data = gitHubIntegrationService.fetchUserRepositories(userId)
        return ResponseEntity.ok(data)
    }

    @PostMapping("/register")
    fun registerGitHubRepository(
        @RequestParam("userId") userId: Long,
        @RequestParam("gitHubRepoId") gitHubRepoId: Long
    ): ResponseEntity<Unit> {
        repositoryApiService.registerRepository(userId, gitHubRepoId)
        return ResponseEntity.ok(Unit)
    }
}