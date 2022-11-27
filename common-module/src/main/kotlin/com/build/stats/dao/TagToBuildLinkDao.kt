package com.build.stats.dao

import com.build.stats.model.TagToBuildLink
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository


@Repository
interface TagToBuildLinkDao : JpaRepository<TagToBuildLink, Long> {
    /**
     * Finds all [TagToBuildLink] entities by [buildId].
     */
    @Query(value = "select e from TagToBuildLink e where e._buildId = ?1")
    fun findAllByBuild(buildId: Long): List<TagToBuildLink>
}