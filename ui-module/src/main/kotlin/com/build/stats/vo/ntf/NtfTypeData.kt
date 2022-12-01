package com.build.stats.vo.ntf

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty


@JsonIgnoreProperties(ignoreUnknown = true)
data class NtfTypeData(
    @JsonProperty("id") val id: Long? = null,
    @JsonProperty("type") val type: String? = null,
    @JsonProperty("description") val description: String? = null
)
