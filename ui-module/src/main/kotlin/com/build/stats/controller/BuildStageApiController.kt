package com.build.stats.controller

import com.build.stats.dao.BuildStageDao
import com.build.stats.model.BuildStage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/stage/api")
class BuildStageApiController @Autowired constructor(
    private val buildStageDao: BuildStageDao
) {

    @PostMapping("/create")
    fun create(
        @RequestBody stage: BuildStage
    ): ResponseEntity<BuildStage> {
        val data = buildStageDao.save(stage)
        return ResponseEntity.ok(data)
    }
}