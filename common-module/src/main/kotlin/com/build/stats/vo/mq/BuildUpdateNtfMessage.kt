package com.build.stats.vo.mq

import com.build.stats.model.BuildStatus
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class BuildUpdateNtfMessage @JsonCreator constructor(
    @JsonProperty("buildId") val buildId: Long? = null,
    @JsonProperty("buildStatus") val buildStatus: BuildStatus? = null,
    @JsonProperty("stageId") val stageId: Long? = null,
) : Serializable