package com.build.stats.service

import com.build.stats.model.BuildStatus


interface BuildNtfSendingService {
    /**
     * Sends a message to MQ, that processing of the build with [buildId]
     * has reached [status].
     */
    fun sendBuildUpdateMessageToQueue(buildId: Long, status: BuildStatus, stageId: Long? = null)
}