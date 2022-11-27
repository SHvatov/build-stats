package com.build.stats.model

import com.build.stats.model.aware.DescriptionAware
import com.build.stats.utils.RequiredProperty
import javax.persistence.*

private const val TABLE_NAME = "gh_build_stage"


@Entity
@Table(name = TABLE_NAME)
class BuildStage @JvmOverloads constructor(
    id: Long? = null,
    repositoryId: Long? = null,
    code: String? = null,
    name: String? = null,
    description: String? = null,
) : DescriptionAware {
    @field:Id
    @field:Column(name = "id")
    @field:GeneratedValue(strategy = GenerationType.AUTO)
    private var _id: Long? = id

    @delegate:Transient
    @Suppress("JpaAttributeTypeInspection")
    var id: Long by RequiredProperty<BuildStage, Long>(this::_id, mandatory = true)

    @field:Column(name = "repo_id")
    private var _repositoryId: Long? = repositoryId

    @delegate:Transient
    @Suppress("JpaAttributeTypeInspection")
    var repositoryId: Long by RequiredProperty<BuildStage, Long>(this::_repositoryId, mandatory = true)

    @field:Column(name = "code")
    private var _code: String? = code

    @delegate:Transient
    @Suppress("JpaAttributeTypeInspection")
    var code: String by RequiredProperty<BuildStage, String>(this::_code, mandatory = true)

    @field:Column(name = "name")
    @Suppress("CanBePrimaryConstructorProperty")
    override var name: String? = name

    @field:Column(name = "description")
    @Suppress("CanBePrimaryConstructorProperty")
    override var description: String? = description
}

