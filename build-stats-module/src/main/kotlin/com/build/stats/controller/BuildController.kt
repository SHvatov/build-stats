package com.build.stats.controller

import com.build.stats.config.StartupOrder
import com.build.stats.model.Build
import com.build.stats.model.BuildStatus
import com.build.stats.model.RepoConfig
import com.build.stats.service.BuildProcessingService
import com.build.stats.vo.StartBuildRq
import com.build.stats.vo.StartBuildRs
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.annotation.Order
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/build")
@Order(StartupOrder.APP_ENDPOINTS_READY)
class BuildController @Autowired constructor(
    private val buildProcessingService: BuildProcessingService
) {
    /**
     * Creates a new [Build] in the system for the repository based on the provided [buildRq].
     * Initially, [Build] is created with [BuildStatus.IN_PROGRESS]. If after configured
     * in [RepoConfig.buildTimeout] time build is not updated, then status is set to [BuildStatus.TIMEOUT].
     * Returns the token of the build in the system, so that it can be later used to update the status
     * of this build.
     */
    @PostMapping(value = ["/start"])
    fun startBuild(@RequestBody buildRq: StartBuildRq): ResponseEntity<StartBuildRs> {
        return ResponseEntity.ok(
            buildProcessingService.startBuild(buildRq)
        )
    }

    /**
     * Starts a new stage of the not-terminated build and finishes prevoius one.
     */
    @PostMapping(value = ["/stage/start"])
    fun startStage(
        @RequestParam("buildToken") buildToken: String,
        @RequestParam("stageCode") stageCode: String
    ): ResponseEntity<Unit> {
        return ResponseEntity.ok(Unit).also {
            buildProcessingService.startStage(buildToken, stageCode)
        }
    }

    /**
     * Updates the [buildStatus] of the [Build] with [buildToken].
     */
    @PostMapping(value = ["/terminate"])
    fun updateBuild(
        @RequestParam("buildToken") buildToken: String,
        @RequestParam("status") buildStatus: BuildStatus
    ): ResponseEntity<Unit> {
        return ResponseEntity.ok(Unit).also {
            buildProcessingService.terminateBuild(buildToken, buildStatus)
        }
    }
}