package com.build.stats.dao

import com.build.stats.model.BuildStatistics
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository


@Repository
interface BuildStatisticsDao : JpaRepository<BuildStatistics, Long> {
    /**
     * Finds all the [BuildStatistics] entities by [repositoryId].
     */
    @Query(value = "select e from BuildStatistics e where e._repositoryId = ?1")
    fun findByRepository(repositoryId: Long): BuildStatistics?
}