package com.build.stats.dao

import com.build.stats.model.NtfType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository


@Repository
interface NtfTypeDao : JpaRepository<NtfType, Long> {
    @Query(value = "select e from NtfType e where e._type = ?1")
    fun findUniqueByType(type: String): NtfType?
}