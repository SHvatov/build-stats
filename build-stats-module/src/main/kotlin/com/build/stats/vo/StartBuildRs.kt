package com.build.stats.vo

import com.build.stats.model.Build
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * VO object, which is used both to create a new [Build].
 * [repoToken] is considered to be required, other elements are optional.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class StartBuildRs @JsonCreator constructor(
    @JsonProperty("repoToken") val repoToken: String? = null,
    @JsonProperty("buildToken") val buildToken: String? = null,
    @JsonProperty("runId") val runId: String? = null, // GITHUB_RUN_ID
) {
    init {
        require(!repoToken.isNullOrBlank()) {
            "\"repoToken\" must be present in the request body"
        }

        require(!buildToken.isNullOrBlank()) {
            "\"buildToken\" must be present in the request body"
        }
    }
}