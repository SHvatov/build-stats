package com.build.stats.model

import com.build.stats.utils.RequiredProperty
import javax.persistence.*

private const val TABLE_NAME = "gh_user_repo_link"

/**
 * Links user and repository.
 * */
@Entity
@Table(name = TABLE_NAME)
class UserToRepoLink @JvmOverloads constructor(
    id: Long? = null,
    userId: Long? = null,
    repositoryId: Long? = null,
) {
    @field:Id
    @field:Column(name = "id")
    @field:GeneratedValue(strategy = GenerationType.AUTO)
    private var _id: Long? = id

    @delegate:Transient
    @Suppress("JpaAttributeTypeInspection")
    var id: Long by RequiredProperty<UserToRepoLink, Long>(this::_id, mandatory = true)

    @field:Column(name = "user_id")
    private var _userId: Long? = userId

    @delegate:Transient
    @Suppress("JpaAttributeTypeInspection")
    var userId: Long by RequiredProperty<UserToRepoLink, Long>(this::_userId, mandatory = true)

    @field:Column(name = "repo_id")
    private var _repositoryId: Long? = repositoryId

    @delegate:Transient
    @Suppress("JpaAttributeTypeInspection")
    var repositoryId: Long by RequiredProperty<UserToRepoLink, Long>(this::_repositoryId, mandatory = true)
}
