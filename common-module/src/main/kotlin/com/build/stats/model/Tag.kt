package com.build.stats.model

import com.build.stats.model.aware.DescriptionAware
import com.build.stats.utils.RequiredProperty
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.Transient

private const val TABLE_NAME = "gh_tag"


@Entity
@Table(name = TABLE_NAME)
class Tag @JvmOverloads constructor(
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
    var id: Long by RequiredProperty<Tag, Long>(this::_id, mandatory = true)

    @field:Column(name = "repo_id")
    private var _repositoryId: Long? = repositoryId

    @delegate:Transient
    @Suppress("JpaAttributeTypeInspection")
    var repositoryId: Long by RequiredProperty<Tag, Long>(this::_repositoryId, mandatory = true)

    @field:Column(name = "code")
    private var _code: String? = code

    @delegate:Transient
    @Suppress("JpaAttributeTypeInspection")
    var code: String by RequiredProperty<Tag, String>(this::_code, mandatory = true)

    @field:Column(name = "name")
    @Suppress("CanBePrimaryConstructorProperty")
    override var name: String? = name

    @field:Column(name = "description")
    @Suppress("CanBePrimaryConstructorProperty")
    override var description: String? = description
}

