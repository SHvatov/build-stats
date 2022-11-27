package com.build.stats.vo

import com.build.stats.model.Build
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * VO object, which is used both to create a new [Build].
 * [repoToken] is considered to be required, other elements are optional.
 * */
@JsonIgnoreProperties(ignoreUnknown = true)
data class StartBuildRq @JsonCreator constructor(
    @JsonProperty("repoToken") val repoToken: String? = null,
    @JsonProperty("creator") val creator: String? = null,
    @JsonProperty("tags") val tags: List<String>? = null,
    @JsonProperty("name") val name: String? = null,
    @JsonProperty("description") val description: String? = null,
    @JsonProperty("runId") val runId: String? = null, // GITHUB_RUN_ID
) {
    init {
        require(!repoToken.isNullOrBlank()) {
            "\"repoToken\" must be present in the request body"
        }
    }
}