package com.build.stats.model

import com.build.stats.utils.RequiredProperty
import java.time.LocalDateTime
import javax.persistence.*

private const val TABLE_NAME = "gh_build_stage_history"


@Entity
@Table(name = TABLE_NAME)
class BuildStageHistory @JvmOverloads constructor(
    id: Long? = null,
    buildId: Long? = null,
    stageId: Long? = null,
    stageStatus: BuildStageStatus? = null,
    started: LocalDateTime? = null,
    finished: LocalDateTime? = null
) {
    @field:Id
    @field:Column(name = "id")
    @field:GeneratedValue(strategy = GenerationType.AUTO)
    private var _id: Long? = id

    @delegate:Transient
    @Suppress("JpaAttributeTypeInspection")
    var id: Long by RequiredProperty<BuildStageHistory, Long>(this::_id, mandatory = true)

    @field:Column(name = "build_id")
    private var _buildId: Long? = buildId

    @delegate:Transient
    @Suppress("JpaAttributeTypeInspection")
    var buildId: Long by RequiredProperty<BuildStageHistory, Long>(this::_buildId, mandatory = true)

    @field:Column(name = "stage_id")
    private var _stageId: Long? = stageId

    @delegate:Transient
    @Suppress("JpaAttributeTypeInspection")
    var stageId: Long by RequiredProperty<BuildStageHistory, Long>(this::_stageId, mandatory = true)

    @field:Enumerated(value = EnumType.STRING)
    @field:Column(name = "status")
    private var _stageStatus: BuildStageStatus? = stageStatus

    @delegate:Transient
    @Suppress("JpaAttributeTypeInspection")
    var stageStatus: BuildStageStatus by RequiredProperty<BuildStageHistory, BuildStageStatus>(
        this::_stageStatus,
        mandatory = true
    )

    @field:Column(name = "started")
    private var _started: LocalDateTime? = started

    @delegate:Transient
    @Suppress("JpaAttributeTypeInspection")
    var started: LocalDateTime by RequiredProperty<BuildStageHistory, LocalDateTime>(this::_started, mandatory = true)

    @field:Column(name = "finished")
    private var _finished: LocalDateTime? = finished

    @delegate:Transient
    @Suppress("JpaAttributeTypeInspection")
    var finished: LocalDateTime by RequiredProperty<BuildStageHistory, LocalDateTime>(this::_finished, mandatory = true)
}

enum class BuildStageStatus {
    IN_PROGRESS,
    COMPLETED,
    FAILED
}
