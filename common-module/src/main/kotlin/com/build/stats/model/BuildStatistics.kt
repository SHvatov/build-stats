package com.build.stats.model

import com.build.stats.model.converter.JsonAttributeConverter
import com.build.stats.utils.RequiredProperty
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.*

private const val TABLE_NAME = "gh_repo_build_stats"


@Entity
@Table(name = TABLE_NAME)
class BuildStatistics @JvmOverloads constructor(
    id: Long? = null,
    repositoryId: Long? = null,
    averageBuildTime: Long? = null,
    maxBuildTime: Long? = null,
    minBuildTime: Long? = null,
    totalBuildsNum: Long? = null,
    failedBuildsNum: Long? = null,
    contributors: RepoContributors? = null,
    tags: RepoTags? = null,
) {
    @field:Id
    @field:Column(name = "id")
    @field:GeneratedValue(strategy = GenerationType.AUTO)
    private var _id: Long? = id

    @delegate:Transient
    @Suppress("JpaAttributeTypeInspection")
    var id: Long by RequiredProperty<BuildStatistics, Long>(this::_id, mandatory = true)

    @field:Column(name = "repo_id")
    private var _repositoryId: Long? = repositoryId

    @delegate:Transient
    @Suppress("JpaAttributeTypeInspection")
    var repositoryId: Long by RequiredProperty<BuildStatistics, Long>(this::_repositoryId, mandatory = true)

    @field:Column(name = "avg_time")
    private var _averageBuildTime: Long? = averageBuildTime

    @delegate:Transient
    @Suppress("JpaAttributeTypeInspection")
    var averageBuildTime: Long by RequiredProperty<BuildStatistics, Long>(this::_averageBuildTime) { 0L }

    @field:Column(name = "max_time")
    private var _maxBuildTime: Long? = maxBuildTime

    @delegate:Transient
    @Suppress("JpaAttributeTypeInspection")
    var maxBuildTime: Long by RequiredProperty<BuildStatistics, Long>(this::_maxBuildTime) { 0L }

    @field:Column(name = "min_time")
    private var _minBuildTime: Long? = minBuildTime

    @delegate:Transient
    @Suppress("JpaAttributeTypeInspection")
    var minBuildTime: Long by RequiredProperty<BuildStatistics, Long>(this::_minBuildTime) { Long.MAX_VALUE }

    @field:Column(name = "total_cnt")
    private var _totalBuildsNum: Long? = totalBuildsNum

    @delegate:Transient
    @Suppress("JpaAttributeTypeInspection")
    var totalBuildsNum: Long by RequiredProperty<BuildStatistics, Long>(this::_totalBuildsNum) { 0L }

    @field:Column(name = "failed_cnt")
    private var _failedBuildsNum: Long? = failedBuildsNum

    @delegate:Transient
    @Suppress("JpaAttributeTypeInspection")
    var failedBuildsNum: Long by RequiredProperty<BuildStatistics, Long>(this::_failedBuildsNum) { 0L }

    @field:Column(name = "contributors")
    @field:Convert(converter = RepoContributorsConverter::class)
    private var _contributors: RepoContributors? = contributors

    @delegate:Transient
    @Suppress("JpaAttributeTypeInspection")
    var contributors: RepoContributors by RequiredProperty<BuildStatistics, RepoContributors>(this::_contributors) { RepoContributors() }

    @field:Column(name = "tags")
    @field:Convert(converter = RepoTagsConverter::class)
    private var _tags: RepoTags? = tags

    @delegate:Transient
    @Suppress("JpaAttributeTypeInspection")
    var tags: RepoTags by RequiredProperty<BuildStatistics, RepoTags>(this::_tags) { RepoTags() }
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class RepoContributor @JsonCreator constructor(
    @JsonProperty("username") val username: String? = null,
    @JsonProperty("builds") val builds: Long? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class RepoContributors @JsonCreator constructor(
    @JsonProperty("contributors") val contributors: List<RepoContributor>? = null
) {
    fun toContributorsMap(): Map<String, Long> {
        return if (contributors.isNullOrEmpty()) {
            emptyMap()
        } else {
            contributors.associateBy { it.username!! }
                .mapValues { it.value.builds!! }
        }
    }

    companion object {
        fun fromContributorsMap(contributorsMap: Map<String, Long>): RepoContributors {
            return contributorsMap.entries
                .map { RepoContributor(it.key, it.value) }
                .let { RepoContributors(it) }
        }
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class RepoTag @JsonCreator constructor(
    @JsonProperty("tagId") val tagId: Long? = null,
    @JsonProperty("builds") val builds: Long? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class RepoTags @JsonCreator constructor(
    @JsonProperty("tags") val tags: List<RepoTag>? = null
) {
    fun toTagsMap(): Map<Long, Long> {
        return if (tags.isNullOrEmpty()) {
            emptyMap()
        } else {
            tags.associateBy { it.tagId!! }
                .mapValues { it.value.builds!! }
        }
    }

    companion object {
        fun fromTagsMap(contributorsMap: Map<Long, Long>): RepoTags {
            return contributorsMap.entries
                .map { RepoTag(it.key, it.value) }
                .let { RepoTags(it) }
        }
    }
}

@Converter(autoApply = true)
class RepoContributorsConverter : JsonAttributeConverter<RepoContributors>()

@Converter(autoApply = true)
class RepoTagsConverter : JsonAttributeConverter<RepoTags>()
