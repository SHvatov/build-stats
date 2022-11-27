package com.build.stats.dao

import com.build.stats.extension.TokenAwareRepositoryExtension
import com.build.stats.model.Build
import com.build.stats.model.BuildStatus
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository


@Repository
interface BuildDao : JpaRepository<Build, Long>, TokenAwareRepositoryExtension<Build> {

    private companion object {
        const val ACTIVE_WITH_TIMEOUT_QUERY =
            "select gb.id\n" +
                    "from gh_build gb\n" +
                    "         join gh_repository gr on gb.repo_id = gr.id\n" +
                    "where gb.status = 'IN_PROGRESS'\n" +
                    "  and EXTRACT(MILLISECONDS FROM (current_timestamp - gb.started)) >=\n" +
                    "      (gr.config::json ->> 'buildTimeout')::numeric;"
    }
}