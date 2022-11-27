package com.build.stats.service.impl

import com.build.stats.dao.BuildDao
import com.build.stats.dao.BuildStageDao
import com.build.stats.dao.BuildStageHistoryDao
import com.build.stats.dao.RepoDao
import com.build.stats.dao.TagDao
import com.build.stats.dao.TagToBuildLinkDao
import com.build.stats.model.Build
import com.build.stats.model.BuildStageHistory
import com.build.stats.model.BuildStageStatus
import com.build.stats.model.BuildStatus
import com.build.stats.model.RepoConfig.Companion.DEFAULT_BUILD_TIMEOUT
import com.build.stats.model.TagToBuildLink
import com.build.stats.service.BuildNtfSendingService
import com.build.stats.service.BuildProcessingService
import com.build.stats.utils.orElse
import com.build.stats.utils.retrieveRequired
import com.build.stats.vo.StartBuildRq
import com.build.stats.vo.StartBuildRs
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.concurrent.ConcurrentHashMap


@Service
class BuildProcessingServiceImpl @Autowired constructor(
    private val buildNtfSendingService: BuildNtfSendingService,
    private val buildStageHistoryDao: BuildStageHistoryDao,
    private val tagToBuildLinkDao: TagToBuildLinkDao,
    private val buildStageDao: BuildStageDao,
    private val buildDao: BuildDao,
    private val repoDao: RepoDao,
    private val tagDao: TagDao,
) : BuildProcessingService {
    private val timeoutCache = ConcurrentHashMap<String, BuildTimeoutData>()

    override fun startBuild(buildVo: StartBuildRq): StartBuildRs = runWithTimestamp { now ->
        val repo = repoDao.findByToken(buildVo.repoToken!!)
        require(repo != null) {
            "Token \"${buildVo.repoToken}\" is not associated with an existing repository"
        }

        val tags = tagDao.findAllByRepository(repo.id)
            .filter { buildVo.tags?.contains(it.code) ?: false }

        require(buildVo.tags == null || buildVo.tags.size == tags.size) {
            "Unknown tags have been provided - ${buildVo.tags}"
        }

        val build = buildDao.save(
            Build(
                status = BuildStatus.IN_PROGRESS,
                started = now,
                repositoryId = repo.id,
                creator = buildVo.creator,
                name = buildVo.name,
                description = buildVo.description,
                link = LINK_TEMPLATE.format(repo.link, buildVo.runId)
            )
        )

        val timeout = repo.config.buildTimeout.orElse { DEFAULT_BUILD_TIMEOUT }
        timeoutCache[build.token] = BuildTimeoutData(build.token, timeout, now)
        buildNtfSendingService.sendBuildUpdateMessageToQueue(build.id, BuildStatus.IN_PROGRESS)

        val tagsToBuildLinks = tags.map { TagToBuildLink(tagId = it.id, buildId = build.id) }
        if (tagsToBuildLinks.isNotEmpty()) {
            tagToBuildLinkDao.saveAll(tagsToBuildLinks)
        }

        return StartBuildRs(
            repoToken = repo.token,
            buildToken = build.token,
            runId = buildVo.runId
        )
    }

    override fun startStage(buildToken: String, stageCode: String) = runWithTimestamp { now ->
        val build = buildDao.findByToken(buildToken).retrieveRequired {
            "Token \"${buildToken}\" is not associated with an existing build"
        }.also {
            validateBuild(it, now)
        }

        terminateCurrentStage(build, now)
        val newStage = buildStageDao.findByRepositoryAndCode(build.repositoryId, stageCode)
            .retrieveRequired {
                "Could not find stage with code [$stageCode] for " +
                    "the repository build is associated with"
            }
        buildStageHistoryDao.save(
            BuildStageHistory(
                buildId = build.id,
                stageId = newStage.id,
                started = now,
                stageStatus = BuildStageStatus.IN_PROGRESS
            )
        )
        buildNtfSendingService.sendBuildUpdateMessageToQueue(build.id, build.status, newStage.id)
    }

    private fun validateBuild(build: Build, now: LocalDateTime) {
        val timeoutData = timeoutCache[build.token]
        if (timeoutData != null && timeoutData.isTimeout(now)) {
            throw IllegalStateException("Build with this ${build.token} as already exceeded its timeout")
        }

        val currentStatus = build.status
        require(!currentStatus.terminal) {
            "Can't update build. Invalid status - $currentStatus"
        }
    }

    private fun terminateCurrentStage(build: Build, now: LocalDateTime) {
        val currentStage = buildStageHistoryDao.findByBuild(build.id)
            .filter { it.stageStatus == BuildStageStatus.IN_PROGRESS }
            .also {
                if (it.size > 1) {
                    throw IllegalStateException("Multiple stages are in progress for build (${build.id})")
                }
            }.firstOrNull() ?: return

        currentStage.apply {
            stageStatus = if (build.status.negative) {
                BuildStageStatus.FAILED
            } else BuildStageStatus.COMPLETED
            finished = now
        }

        buildStageHistoryDao.save(currentStage)
    }

    private inline fun <R> runWithTimestamp(block: (LocalDateTime) -> R): R {
        val now = LocalDateTime.now()
        return block(now)
    }

    private data class BuildTimeoutData(
        val token: String,
        private val timeout: Long,
        private val created: LocalDateTime
    ) {
        fun isTimeout(now: LocalDateTime): Boolean =
            created.plusNanos(timeout * 1000).isAfter(now)
    }

    companion object {
        const val LINK_TEMPLATE = "%s/runs/%s"
    }
}