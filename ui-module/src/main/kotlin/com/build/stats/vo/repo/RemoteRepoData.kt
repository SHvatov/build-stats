package com.build.stats.vo.repo

import com.build.stats.model.Repo
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty


@JsonIgnoreProperties(ignoreUnknown = true)
data class RemoteRepoData(
    @JsonProperty("id") val gitHubId: Long? = null,
    @JsonProperty("full_name") val fullName: String? = null,
    @JsonProperty("name") val name: String? = null,
    @JsonProperty("description") val description: String? = null,
) {
    @JsonIgnore
    fun toRepoEntity(): Repo =
        Repo(
            gitHubId = gitHubId,
            name = fullName,
            link = "$GITHUB_REPO_URL_TEMPLATE$fullName",
            description = description
        )

    private companion object {
        const val GITHUB_REPO_URL_TEMPLATE = "https://github.com/"
    }
}