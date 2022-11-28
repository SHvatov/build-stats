package com.build.stats.notification.service.impl

import com.build.stats.dao.BuildDao
import com.build.stats.dao.BuildNtfUserPreferencesDao
import com.build.stats.dao.NtfTypeDao
import com.build.stats.dao.UserDao
import com.build.stats.notification.model.NtfTypeCode
import com.build.stats.notification.service.NtfInfoService
import com.build.stats.notification.service.NtfSender
import com.build.stats.utils.findRequiredById
import com.build.stats.utils.retrieveRequired
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class NtfInfoServiceImpl @Autowired constructor(
    val buildDao: BuildDao,
    val buildNtfUserPreferencesDao: BuildNtfUserPreferencesDao,
    val ntfTypeDao: NtfTypeDao,
    val userDao: UserDao
) : NtfInfoService {
    private lateinit var ntfSendersByType: Map<NtfTypeCode, NtfSender>

    @Autowired
    fun setNtfSenders(ntfSenders: List<NtfSender>) {
        ntfSendersByType = ntfSenders.associateBy { it.ntfTypeCode }
    }

    override fun prepareNtfDestinations(buildId: Long): Map<NtfTypeCode, List<String>> {
        val build = buildDao.findRequiredById(buildId)

        val buildNtfUserPreferences = buildNtfUserPreferencesDao.findByRepositoryId(build.repositoryId)

        val preferencesByUserId = buildNtfUserPreferences
            .associateBy { it.userId }
            .mapValues { (_, v) -> v.config.preferredNtfTypeId.retrieveRequired() }

        val result = mutableMapOf<NtfTypeCode, MutableList<String>>()

        val usersToNotifyById = userDao.findAllById(preferencesByUserId.keys)
        val ntfTypesById = ntfTypeDao.findAllById(preferencesByUserId.values)
            .associateBy { it.id }

        usersToNotifyById.forEach {
            val preferredNtfTypeId = preferencesByUserId.getValue(it.id)
            val ntfType = ntfTypesById.getValue(preferredNtfTypeId).let {
                enumValueOf<NtfTypeCode>(it.type)
            }

            result.putIfAbsent(ntfType, mutableListOf())
            ntfSendersByType.getValue(ntfType).fetchNtfDestination(it)?.also {
                result.getValue(ntfType).add(it)
            }
        }

        return result
    }
}