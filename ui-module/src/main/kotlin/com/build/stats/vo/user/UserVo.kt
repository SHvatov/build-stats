package com.build.stats.vo.user

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty


@JsonIgnoreProperties(ignoreUnknown = true)
data class UserVo(
    @JsonProperty("id") val id: Long? = null,
    @JsonProperty("username") val username: String? = null,
)