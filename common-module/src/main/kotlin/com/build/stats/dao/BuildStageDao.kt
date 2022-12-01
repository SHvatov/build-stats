package com.build.stats.dao

import com.build.stats.model.BuildStage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository


@Repository
interface BuildStageDao : JpaRepository<BuildStage, Long> {
    /**
     * Finds all the entities by [repositoryId].
     */
    @Query(value = "select e from BuildStage e where e._repositoryId = ?1")
    fun findAllByRepository(repositoryId: Long): List<BuildStage>

    /**
     * Finds all the entities by [repositoryId].
     */
    @Query(value = "select e from BuildStage e where e._repositoryId = ?1 and e._code = ?2")
    fun findByRepositoryAndCode(repositoryId: Long, code: String): BuildStage
}