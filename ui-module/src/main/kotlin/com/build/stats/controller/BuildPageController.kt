package com.build.stats.controller

import com.build.stats.config.OAuth2GitHubAttrs
import com.build.stats.dao.*
import com.build.stats.model.Build
import com.build.stats.model.BuildStatus
import com.build.stats.model.TagToBuildLink
import com.build.stats.utils.getPage
import com.build.stats.utils.retrieveRequired
import com.build.stats.utils.retrieveRequiredAs
import com.build.stats.vo.ntf.NtfTypeData
import com.build.stats.vo.repo.RegisteredRepoData
import com.build.stats.vo.stat.BuildStatsData
import com.build.stats.vo.user.UserVo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam


@Controller
class BuildPageController @Autowired constructor(
    private val userDao: UserDao,
    private val userToRepoLinkDao: UserToRepoLinkDao,
    private val repoDao: RepoDao,
    private val ntfTypeDao: NtfTypeDao,
    private val buildNtfUserPreferencesDao: BuildNtfUserPreferencesDao,
    private val tagDao: TagDao,
    private val buildStatisticsDao: BuildStatisticsDao,
    private val buildDao: BuildDao,
    private val tagToBuildLinkDao: TagToBuildLinkDao,
    private val buildStageDao: BuildStageDao
) {
    @RequestMapping("/builds")
    fun buildListPage(
        @RequestParam repoId: Long,
        model: Model,
        @AuthenticationPrincipal oAuthUser: DefaultOAuth2User
    ): String {
        val email = oAuthUser.attributes[OAuth2GitHubAttrs.EMAIL]
            .retrieveRequiredAs<Any?, String> {
                "Email must be present in the authentication attributes"
            }
        val systemUser = userDao.findByEmail(email)
            .retrieveRequired {
                "No user is associated with $email in teh system"
            }
        require(userToRepoLinkDao.findAllByUserAndRepo(systemUser.id, repoId) != null) {
            "Error! This repo is not yours!"
        }
        val repo = repoDao.findByIdOrNull(repoId)
            .retrieveRequired {
                "No repo with id $repoId"
            }

        val ntfTypes = ntfTypeDao.findAll().map {
            NtfTypeData(
                id = it.id,
                type = it.type,
                description = it.description

            )
        }

        val contributors = userToRepoLinkDao.findAllByRepo(repoId)
            .map { it.userId }
            .map { userDao.findByIdOrNull(it) }
            .map { it!!.username }

        val ntfUserPrefs = buildNtfUserPreferencesDao.findUniqueRepositoryIdAndUserId(repoId, systemUser.id)

        val tags = tagDao.findAllByRepository(repo.id)

        val stages = buildStageDao.findAllByRepository(repo.id)

        val stats = buildStatisticsDao.findByRepository(repo.id)

        val lastFiveBuilds = buildDao.findAllByRepository(repo.id)
            .sortedByDescending { it.id }
            .getPage(1, 5)

        val builds = buildDao
            .findAllByRepository(repo.id)

        val buildsByStatus = mutableMapOf<BuildStatus, MutableList<Build>>()

        val tagsToBuildsLinks = mutableListOf<TagToBuildLink>()

        builds.forEach {
            if (buildsByStatus.containsKey(it.status)) {
                buildsByStatus[it.status]!!.add(it)
            } else {
                buildsByStatus[it.status] = mutableListOf(it)
            }
            tagsToBuildsLinks.addAll(tagToBuildLinkDao.findAllByBuild(it.id))
        }

        val buildsByTag = mutableMapOf<String, MutableList<TagToBuildLink>>()

        tagsToBuildsLinks.forEach {
            if (buildsByTag.containsKey(tagDao.findByIdOrNull(it.tagId)!!.code)) {
                buildsByTag[tagDao.findByIdOrNull(it.tagId)!!.code]!!.add(it)
            } else {
                buildsByTag[tagDao.findByIdOrNull(it.tagId)!!.code] = mutableListOf(it)
            }
        }


        with(repo) {
            model.addAttribute(
                REPO_DATA_ATTR,
                RegisteredRepoData(
                    id = id,
                    name = name,
                    description = description,
                    link = link
                )
            )
            model.addAttribute(
                TOKEN_DATA_ATTR,
                token
            )
        }

        with(systemUser) {
            model.addAttribute(
                USER_DATA_ATTR,
                UserVo(
                    id = id
                )
            )
        }

        model.addAttribute(
            NTF_TYPES_DATA,
            ntfTypes
        )

        model.addAttribute(
            NTF_USER_PREF_INFO,
            ntfUserPrefs
        )

        model.addAttribute(
            CONTRIBUTORS_INFO,
            contributors
        )

        model.addAttribute(
            TAGS_INFO,
            tags
        )

        model.addAttribute(
            STAGES_INFO,
            stages
        )

        with(stats) {
            model.addAttribute(
                STATS_INFO,
                BuildStatsData(
                    id = this!!.id,
                    averageBuildTime = averageBuildTime,
                    maxBuildTime = maxBuildTime,
                    minBuildTime = minBuildTime,
                    totalBuildsNum = totalBuildsNum,
                    failedBuildsNum = failedBuildsNum,
                )
            )
        }

        model.addAttribute(
            LAST_FIVE_BUILDS_INFO,
            lastFiveBuilds
        )

        model.addAttribute(
            BUILDS_BY_STATUS_INFO,
            buildsByStatus
        )

        model.addAttribute(
            BUILDS_BY_TAG_INFO,
            buildsByTag
        )
        return "authorized/builds"
    }

    private companion object {
        const val REPO_DATA_ATTR = "repo"
        const val TOKEN_DATA_ATTR = "token"
        const val USER_DATA_ATTR = "user"
        const val NTF_TYPES_DATA = "ntfTypes"
        const val NTF_USER_PREF_INFO = "ntfPrefsConfig"
        const val CONTRIBUTORS_INFO = "contributors"
        const val TAGS_INFO = "tags"
        const val STAGES_INFO = "stages"
        const val STATS_INFO = "stats"
        const val LAST_FIVE_BUILDS_INFO = "lastFiveBuilds"
        const val BUILDS_BY_STATUS_INFO = "buildsByStatus"
        const val BUILDS_BY_TAG_INFO = "buildsByTag"
    }
}