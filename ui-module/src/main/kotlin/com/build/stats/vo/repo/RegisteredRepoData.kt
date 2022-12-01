package com.build.stats.vo.repo

import com.build.stats.model.BuildStatus
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime


@JsonIgnoreProperties(ignoreUnknown = true)
data class RegisteredRepoData(
    @JsonProperty("id") val id: Long? = null,
    @JsonProperty("name") val name: String? = null,
    @JsonProperty("description") val description: String? = null,
    @JsonProperty("link") val link: String? = null,
    @JsonProperty("lastBuildStatus") val lastBuildStatus: BuildStatus? = null,
    @JsonProperty("lastBuildTime") val lastBuildTime: LocalDateTime? = null,
)