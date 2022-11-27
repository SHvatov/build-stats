package com.build.stats.service

import com.build.stats.model.Build
import com.build.stats.model.BuildStatus
import com.build.stats.vo.StartBuildRq
import com.build.stats.vo.StartBuildRs
import javax.transaction.Transactional


interface BuildProcessingService {
    /**
     * Creates a new [Build] based on the provided [buildVo] and returns
     * a [StartBuildRs] with all information, which is required to uniquely
     * identify build. Also registers this build in timeout service and
     * sends a corresponding message to the queue.
     */
    @Transactional
    fun startBuild(buildVo: StartBuildRq): StartBuildRs

    /**
     * Updates the [status] of build with provided [buildToken].
     * Also updates the statistics and send a notification, that this
     * build has been updated. Also finishes current stage of the build if such is present.
     * If build is already in the terminal state, then exception is thrown.
     */
    @Transactional
    fun terminateBuild(buildToken: String, status: BuildStatus)

    /**
     * Starts a new stage with [stageCode] within build with [buildToken].
     * Previous stage is considered to be successfully completed. If build is
     * already in the terminal state, then exception is thrown.
     */
    @Transactional
    fun startStage(buildToken: String, stageCode: String)

    /**
     * Starts a scheduler, which will be checking registered
     * builds if the have failed due to timeout.
     */
    @Transactional
    fun pollTimeoutBuilds()
}