package com.build.stats.service

import com.build.stats.model.Build


interface BuildStatsAggregatorService {
    /**
     * Adds the provided [build] to the overall statistics of the builds of the
     * repository, this build is associated with.
     */
    fun addBuildToStats(build: Build)
}