package com.build.stats.model

import com.build.stats.model.converter.JsonAttributeConverter
import com.build.stats.utils.RequiredProperty
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.Column
import javax.persistence.Convert
import javax.persistence.Converter
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.Transient

private const val TABLE_NAME = "gh_build_ntf_pref"


@Entity
@Table(name = TABLE_NAME)
class BuildNtfUserPreferences @JvmOverloads constructor(
    id: Long? = null,
    userId: Long? = null,
    repositoryId: Long? = null,
    config: BuildNtfUserConfig? = null,
) {
    @field:Id
    @field:Column(name = "id")
    @field:GeneratedValue(strategy = GenerationType.AUTO)
    private var _id: Long? = id

    @delegate:Transient
    @Suppress("JpaAttributeTypeInspection")
    var id: Long by RequiredProperty<BuildNtfUserPreferences, Long>(this::_id, mandatory = true)

    @field:Column(name = "user_id")
    private var _userId: Long? = userId

    @delegate:Transient
    @Suppress("JpaAttributeTypeInspection")
    var userId: Long by RequiredProperty<BuildNtfUserPreferences, Long>(this::_userId, mandatory = true)

    @field:Column(name = "repo_id")
    private var _repositoryId: Long? = repositoryId

    @delegate:Transient
    @Suppress("JpaAttributeTypeInspection")
    var repositoryId: Long by RequiredProperty<BuildNtfUserPreferences, Long>(this::_repositoryId, mandatory = true)

    @field:Column(name = "config")
    @field:Convert(converter = BuildNtfUserConfigConverter::class)
    private var _config: BuildNtfUserConfig? = config

    @delegate:Transient
    @Suppress("JpaAttributeTypeInspection")
    var config: BuildNtfUserConfig by RequiredProperty<BuildNtfUserPreferences, BuildNtfUserConfig>(this::_config) { BuildNtfUserConfig() }
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class BuildNtfUserConfig @JsonCreator constructor(
    @JsonProperty("notifyOnStart") val notifyOnStart: Boolean? = null,
    @JsonProperty("notifyOnFailure") val notifyOnFailure: Boolean? = null,
    @JsonProperty("notifyOnSuccess") val notifyOnSuccess: Boolean? = null,
    @JsonProperty("allowedTags") val allowedTags: List<Long>? = null,
    @JsonProperty("preferredNtfTypeId") val preferredNtfTypeId: Long? = null,
)

@Converter(autoApply = true)
class BuildNtfUserConfigConverter : JsonAttributeConverter<BuildNtfUserConfig>()