package com.build.stats.service.impl

import com.build.stats.dao.BuildStatisticsDao
import com.build.stats.dao.TagToBuildLinkDao
import com.build.stats.model.Build
import com.build.stats.model.BuildStatistics
import com.build.stats.model.RepoContributors.Companion.fromContributorsMap
import com.build.stats.model.RepoTags.Companion.fromTagsMap
import com.build.stats.service.BuildStatsAggregatorService
import com.build.stats.utils.orElse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.temporal.ChronoUnit


@Service
class BuildStatsAggregatorServiceImpl @Autowired constructor(
    private val buildStatisticsDao: BuildStatisticsDao,
    private val tagToBuildLinkDao: TagToBuildLinkDao,
) : BuildStatsAggregatorService {
    override fun addBuildToStats(build: Build) {
        val currentStats = buildStatisticsDao.findByRepository(build.repositoryId)
            ?: BuildStatistics()

        with(currentStats) {
            val totalSpentTime = averageBuildTime * totalBuildsNum
            val buildTime = build.started.until(build.finished, ChronoUnit.MILLIS)
            averageBuildTime = (totalSpentTime + buildTime) / (totalBuildsNum + 1)

            maxBuildTime = buildTime.takeIf { it > maxBuildTime } ?: maxBuildTime

            minBuildTime = buildTime.takeIf { it < minBuildTime } ?: minBuildTime

            totalBuildsNum = totalBuildsNum.orElse { 0L } + 1L
            if (build.status.negative) {
                failedBuildsNum = totalBuildsNum.orElse { 0L } + 1L
            }

            contributors = fromContributorsMap(
                HashMap(contributors.toContributorsMap()).apply {
                    putIfAbsent(build.creator, 0L)
                    computeIfPresent(build.creator) { _, v -> v + 1 }
                }
            )

            tags = fromTagsMap(
                HashMap(tags.toTagsMap()).apply {
                    for (tagId in tagToBuildLinkDao.findAllByBuild(build.id).map { it.tagId }) {
                        putIfAbsent(tagId, 0L)
                        computeIfPresent(tagId) { _, v -> v + 1 }
                    }
                }
            )
        }

        buildStatisticsDao.save(currentStats)
    }
}