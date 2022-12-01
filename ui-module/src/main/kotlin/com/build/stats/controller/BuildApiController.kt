package com.build.stats.controller

import com.build.stats.dao.RepoDao
import com.build.stats.model.Repo
import com.build.stats.model.aware.TokenAware
import com.build.stats.service.BuildApiService
import com.build.stats.vo.build.FilteredBuildData
import com.build.stats.vo.filter.UiFilterData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/build/api")
class BuildApiController @Autowired constructor(
    private val repoDao: RepoDao,
    private val buildApiService: BuildApiService
) {
    @GetMapping("/generateToken/{id}")
    fun update(
        @PathVariable id: Long
    ): ResponseEntity<Repo> {
        val repo = repoDao.findByIdOrNull(id)
        repo!!.token = TokenAware.EMPTY_TOKEN
        repoDao.save(repo)
        return ResponseEntity.ok(repo)
    }

    @PostMapping("/list")
    fun fetchRegisteredRepositories(
        @RequestParam("repoId") repoId: Long,
        @RequestBody(required = false) filter: UiFilterData?
    ): ResponseEntity<List<FilteredBuildData>> {
        val data = buildApiService.prepareFilteredBuildData(repoId, filter)
        return ResponseEntity.ok(data)
    }
}