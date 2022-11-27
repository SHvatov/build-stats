package com.build.stats.service

import com.build.stats.model.Build
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
}