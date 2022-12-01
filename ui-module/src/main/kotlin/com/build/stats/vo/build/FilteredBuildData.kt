package com.build.stats.vo.build

import com.build.stats.model.BuildStageStatus
import com.build.stats.model.BuildStatus
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
data class FilteredBuildData(
    @JsonProperty("id") val id: Long? = null,
    @JsonProperty("name") val name: String? = null,
    @JsonProperty("description") val description: String? = null,
    @JsonProperty("started") val started: LocalDateTime? = null,
    @JsonProperty("finished") val finished: LocalDateTime? = null,
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("creator") val creator: String? = null,
    @JsonProperty("status") val status: BuildStatus? = null,
    @JsonProperty("stages") val stages: List<StageData>? = null,
    @JsonProperty("tags") val tags: List<TagData>? = null,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class StageData(
    @JsonProperty("id") val id: Long? = null,
    @JsonProperty("name") val name: String? = null,
    @JsonProperty("code") val code: String? = null,
    @JsonProperty("status") val status: BuildStageStatus
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class TagData(
    @JsonProperty("id") val id: Long? = null,
    @JsonProperty("code") val code: String? = null,
    @JsonProperty("name") val name: String? = null
)
