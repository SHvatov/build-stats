package com.build.stats.model

import com.build.stats.model.aware.DescriptionAware
import com.build.stats.model.aware.TokenAware
import com.build.stats.model.converter.JsonAttributeConverter
import com.build.stats.utils.RequiredProperty
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.*

private const val TABLE_NAME = "gh_repository"


@Entity
@Table(name = TABLE_NAME)
class Repo @JvmOverloads constructor(
    id: Long? = null,
    gitHubId: Long? = null,
    name: String? = null,
    description: String? = null,
    link: String? = null,
    token: String? = null,
    buildStatisticsId: Long? = null,
    config: RepoConfig? = null,
) : DescriptionAware, TokenAware {
    @field:Id
    @field:Column(name = "id")
    @field:GeneratedValue(strategy = GenerationType.AUTO)
    private var _id: Long? = id

    @delegate:Transient
    @Suppress("JpaAttributeTypeInspection")
    var id: Long by RequiredProperty<Repo, Long>(this::_id, mandatory = true)

    @field:Column(name = "github_id")
    private var _gitHubId: Long? = gitHubId

    @delegate:Transient
    @Suppress("JpaAttributeTypeInspection")
    var gitHubId: Long by RequiredProperty<Repo, Long>(this::_gitHubId, mandatory = true)

    @field:Column(name = "token")
    private var _token: String? = token

    @delegate:Transient
    @Suppress("JpaAttributeTypeInspection")
    override var token: String by RequiredProperty<Repo, String>(this::_token) { TokenAware.EMPTY_TOKEN }

    @field:Column(name = "build_stats_id")
    private var _buildStatisticsId: Long? = buildStatisticsId

    @delegate:Transient
    @Suppress("JpaAttributeTypeInspection")
    var buildStatisticsId: Long by RequiredProperty<Repo, Long>(this::_buildStatisticsId, mandatory = true)

    @field:Column(name = "config")
    @field:Convert(converter = RepoConfigConverter::class)
    private var _config: RepoConfig? = config

    @delegate:Transient
    @Suppress("JpaAttributeTypeInspection")
    var config: RepoConfig by RequiredProperty<Repo, RepoConfig>(this::_config) { RepoConfig() }

    @field:Column(name = "name")
    @Suppress("CanBePrimaryConstructorProperty")
    override var name: String? = name

    @field:Column(name = "description")
    @Suppress("CanBePrimaryConstructorProperty")
    override var description: String? = description

    @field:Column(name = "link")
    @Suppress("CanBePrimaryConstructorProperty")
    var link: String? = link
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class RepoConfig @JsonCreator constructor(
    @JsonProperty("buildTimeout") var buildTimeout: Long? = null
) {
    companion object {
        const val DEFAULT_BUILD_TIMEOUT = 60 * 1000L
    }
}

@Converter(autoApply = true)
class RepoConfigConverter : JsonAttributeConverter<RepoConfig>()
