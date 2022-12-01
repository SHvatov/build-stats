package com.build.stats.vo.stat

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty


@JsonIgnoreProperties(ignoreUnknown = true)
data class BuildStatsData(
    @JsonProperty("id") val id: Long? = null,
    @JsonProperty("averageBuildTime") val averageBuildTime: Long? = null,
    @JsonProperty("maxBuildTime") val maxBuildTime: Long? = null,
    @JsonProperty("minBuildTime") val minBuildTime: Long? = null,
    @JsonProperty("totalBuildsNum") val totalBuildsNum: Long? = null,
    @JsonProperty("failedBuildsNum") val failedBuildsNum: Long? = null,
)
