package com.build.stats.dao

import com.build.stats.model.BuildStageHistory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository


@Repository
interface BuildStageHistoryDao : JpaRepository<BuildStageHistory, Long> {
    /**
     * Finds all [BuildStageHistory] entities by [buildId].
     */
    @Query("select e from BuildStageHistory e where e._buildId = ?1")
    fun findByBuild(buildId: Long): List<BuildStageHistory>
}