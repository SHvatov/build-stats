package com.build.stats.model

import com.build.stats.utils.RequiredProperty
import javax.persistence.*

private const val TABLE_NAME = "gh_tag_build_link"


@Entity
@Table(name = TABLE_NAME)
class TagToBuildLink @JvmOverloads constructor(
    id: Long? = null,
    tagId: Long? = null,
    buildId: Long? = null,
) {
    @field:Id
    @field:Column(name = "id")
    @field:GeneratedValue(strategy = GenerationType.AUTO)
    private var _id: Long? = id

    @delegate:Transient
    @Suppress("JpaAttributeTypeInspection")
    var id: Long by RequiredProperty<TagToBuildLink, Long>(this::_id, mandatory = true)

    @field:Column(name = "tag_id")
    private var _tagId: Long? = tagId

    @delegate:Transient
    @Suppress("JpaAttributeTypeInspection")
    var tagId: Long by RequiredProperty<TagToBuildLink, Long>(this::_tagId, mandatory = true)

    @field:Column(name = "build_id")
    private var _buildId: Long? = buildId

    @delegate:Transient
    @Suppress("JpaAttributeTypeInspection")
    var buildId: Long by RequiredProperty<TagToBuildLink, Long>(this::_buildId, mandatory = true)
}
