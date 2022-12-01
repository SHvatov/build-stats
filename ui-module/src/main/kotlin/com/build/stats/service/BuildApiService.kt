package com.build.stats.service

import com.build.stats.vo.build.FilteredBuildData
import com.build.stats.vo.filter.UiFilterData


interface BuildApiService {
    fun prepareFilteredBuildData(repoId: Long, filter: UiFilterData?): List<FilteredBuildData>
}