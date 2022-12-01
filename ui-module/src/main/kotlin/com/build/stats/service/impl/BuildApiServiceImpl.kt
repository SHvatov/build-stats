package com.build.stats.service.impl

import com.build.stats.dao.*
import com.build.stats.model.BuildStatus
import com.build.stats.service.BuildApiService
import com.build.stats.utils.getPage
import com.build.stats.utils.prepareInterval
import com.build.stats.vo.build.FilteredBuildData
import com.build.stats.vo.build.StageData
import com.build.stats.vo.build.TagData
import com.build.stats.vo.filter.UiFilterData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime


@Service
class BuildApiServiceImpl @Autowired constructor(
    private val buildDao: BuildDao,
    private val tagDao: TagDao,
    private val tagToBuildLinkDao: TagToBuildLinkDao,
    private val buildStageDao: BuildStageDao,
    private val buildStageHistoryDao: BuildStageHistoryDao
) : BuildApiService {
    override fun prepareFilteredBuildData(repoId: Long, filter: UiFilterData?): List<FilteredBuildData> {

        val buildData = buildDao.findAllByRepository(repoId)
            .map { build ->
                val tags = tagToBuildLinkDao.findAllByBuild(build.id)
                    .map { tag ->
                        val tagInfo = tagDao.findByIdOrNull(tag.tagId)
                        TagData(tagInfo!!.id, tagInfo.code, tagInfo.name)
                    }

                val stages = buildStageHistoryDao.findByBuild(build.id)
                    .map { stage ->
                        val stageInfo = buildStageDao.findByIdOrNull(stage.stageId)
                        StageData(stageInfo!!.id, stageInfo.name, stageInfo.code, stage.stageStatus)
                    }

                FilteredBuildData(
                    build.id,
                    build.name,
                    build.description,
                    build.started,
                    build.finished,
                    build.link,
                    build.creator,
                    build.status,
                    stages,
                    tags
                )
            }

        return buildData.filter { build ->
            if (filter != null) {
                var status = filter.getAttribute<String>(STATUS_ATTR)
                if (status == "select") {
                    status = null
                }
                var tag = filter.getAttribute<String>(TAG_ATTR)
                if (tag == "select") {
                    tag = null
                }
                val buildStartBefore = filter.getAttribute<LocalDateTime>(BUILD_START_BEFORE_ATTR)
                val buildStartAfter = filter.getAttribute<LocalDateTime>(BUILD_START_AFTER_ATTR)

                val isBuildDateFilterPresent = buildStartAfter != null || buildStartBefore != null
                if (isBuildDateFilterPresent) {
                    val isInDateRange = build.started!! in prepareInterval(buildStartAfter, buildStartBefore)
                    if (!isInDateRange) {
                        return@filter false
                    }
                }

                val tagFlag = tag?.let {
                    build.tags!!.filter { it.code == tag }.isNotEmpty()
                } ?: true

                val statusFlag = status?.let {
                    build.status?.equals(BuildStatus.valueOf(status))
                } ?: true

                return@filter tagFlag && statusFlag
            } else true
        }.let {
            if (filter?.isPaged == true) {
                it.getPage(filter.page, filter.pageSize)
            } else it
        }
    }

    private companion object {

        const val STATUS_ATTR = "status"

        const val TAG_ATTR = "tag"

        const val BUILD_START_BEFORE_ATTR = "buildStartBefore"

        const val BUILD_START_AFTER_ATTR = "buildStartAfter"
    }
}
