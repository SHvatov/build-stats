package com.build.stats.notification.service

import com.build.stats.notification.model.NtfTypeCode


interface NtfInfoService {

    fun prepareNtfDestinations(buildId: Long): Map<NtfTypeCode, List<String>>
}