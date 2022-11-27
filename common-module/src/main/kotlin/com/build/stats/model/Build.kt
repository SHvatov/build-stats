package com.build.stats.model

import com.build.stats.model.aware.DescriptionAware
import com.build.stats.model.aware.TokenAware
import com.build.stats.utils.RequiredProperty
import java.time.LocalDateTime
import javax.persistence.*

private const val TABLE_NAME = "gh_build"


@Entity
@Table(name = TABLE_NAME)
class Build @JvmOverloads constructor(
    id: Long? = null,
    started: LocalDateTime? = null,
    finished: LocalDateTime? = null,
    repositoryId: Long? = null,
    status: BuildStatus? = null,
    creator: String? = null,
    name: String? = null,
    description: String? = null,
    link: String? = null,
    token: String? = null,
) : DescriptionAware, TokenAware {
    @field:Id
    @field:Column(name = "id")
    @field:GeneratedValue(strategy = GenerationType.AUTO)
    private var _id: Long? = id

    @delegate:Transient
    @Suppress("JpaAttributeTypeInspection")
    var id: Long by RequiredProperty<Build, Long>(this::_id, mandatory = true)

    @field:Column(name = "started")
    private var _started: LocalDateTime? = started

    @delegate:Transient
    @Suppress("JpaAttributeTypeInspection")
    var started: LocalDateTime by RequiredProperty<Build, LocalDateTime>(this::_started, mandatory = true)

    @field:Column(name = "finished")
    private var _finished: LocalDateTime? = finished

    @delegate:Transient
    @Suppress("JpaAttributeTypeInspection")
    var finished: LocalDateTime by RequiredProperty<Build, LocalDateTime>(this::_finished, mandatory = true)

    @field:Column(name = "repo_id")
    private var _repositoryId: Long? = repositoryId

    @delegate:Transient
    @Suppress("JpaAttributeTypeInspection")
    var repositoryId: Long by RequiredProperty<Build, Long>(this::_repositoryId, mandatory = true)

    @field:Enumerated(value = EnumType.STRING)
    @field:Column(name = "status")
    private var _status: BuildStatus? = status

    @delegate:Transient
    @Suppress("JpaAttributeTypeInspection")
    var status: BuildStatus by RequiredProperty<Build, BuildStatus>(this::_status, mandatory = true)

    @field:Column(name = "token")
    private var _token: String? = token

    @delegate:Transient
    @Suppress("JpaAttributeTypeInspection")
    override var token: String by RequiredProperty<Build, String>(this::_token) { TokenAware.EMPTY_TOKEN }

    @field:Column(name = "creator")
    @Suppress("CanBePrimaryConstructorProperty")
    var creator: String? = creator

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

enum class BuildStatus(val negative: Boolean, val terminal: Boolean = true) {
    IN_PROGRESS(false, false),
    SUCCESS(false),
    FAILURE(true),
    TIMEOUT(true),
    APP_ERROR(true)
}