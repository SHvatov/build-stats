package com.build.stats.dao

import com.build.stats.model.Tag
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository


@Repository
interface TagDao : JpaRepository<Tag, Long> {
    /**
     * Finds all the entities by [repositoryId].
     */
    @Query(value = "select e from Tag e where e._repositoryId = ?1")
    fun findAllByRepository(repositoryId: Long): List<Tag>
}