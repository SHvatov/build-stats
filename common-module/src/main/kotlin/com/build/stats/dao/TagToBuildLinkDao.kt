package com.build.stats.dao

import com.build.stats.model.TagToBuildLink
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository


@Repository
interface TagToBuildLinkDao : JpaRepository<TagToBuildLink, Long> {
}