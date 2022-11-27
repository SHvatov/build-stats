package com.build.stats.model

import com.build.stats.utils.RequiredProperty
import javax.persistence.*


private const val TABLE_NAME = "gh_ntf_types"

@Entity
@Table(name = TABLE_NAME)
class NtfType @JvmOverloads constructor(
    id: Long? = null,
    type: String? = null,
    description: String? = null
) {
    @field:Id
    @field:Column(name = "id")
    @field:GeneratedValue(strategy = GenerationType.AUTO)
    private var _id: Long? = id

    @delegate:Transient
    @Suppress("JpaAttributeTypeInspection")
    var id: Long by RequiredProperty<NtfType, Long>(this::_id, mandatory = true)

    @field:Column(name = "type")
    private var _type: String? = type

    @delegate:Transient
    @Suppress("JpaAttributeTypeInspection")
    var type: String by RequiredProperty<NtfType, String>(this::_type, mandatory = true)

    @field:Column(name = "description")
    private var _description: String? = description

    @delegate:Transient
    @Suppress("JpaAttributeTypeInspection")
    var description: String by RequiredProperty<NtfType, String>(this::_description, mandatory = true)
}



