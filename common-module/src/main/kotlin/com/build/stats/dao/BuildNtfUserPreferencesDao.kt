package com.build.stats.dao

import com.build.stats.model.BuildNtfUserPreferences
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository


@Repository
interface BuildNtfUserPreferencesDao : JpaRepository<BuildNtfUserPreferences, Long> {
    @Query(value = "select e from BuildNtfUserPreferences e where e._repositoryId = ?1")
    fun findByRepositoryId(repositoryId: Long): List<BuildNtfUserPreferences>
}