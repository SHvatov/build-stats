package com.build.stats.notification.service.impl

import com.build.stats.notification.model.NtfTypeCode
import com.build.stats.notification.service.NtfInfoService
import com.build.stats.notification.service.BuildNotificationService
import com.build.stats.notification.service.NtfSender
import com.build.stats.vo.mq.BuildUpdateNtfMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct


@Component
class BuildNotificationServiceImpl @Autowired constructor(
    val ntfInfoService: NtfInfoService,
    val ntfSenders: List<NtfSender>
) : BuildNotificationService {

    private lateinit var ntfSendersByNtfType: Map<NtfTypeCode, NtfSender>

    @PostConstruct
    fun setUp() {
        ntfSendersByNtfType = ntfSenders.associateBy { it.ntfTypeCode }
    }

    override fun sendNotificationsToDestinations(message: BuildUpdateNtfMessage) {
        val ntfConfigMap = ntfInfoService.prepareNtfDestinations(message.buildId!!)

        ntfConfigMap.forEach { (ntfType, notifyTos) ->
            notifyTos.forEach {
                ntfSendersByNtfType[ntfType]!!.send(it, buildMsg(message))
            }
        }
    }

    private fun buildMsg(message: BuildUpdateNtfMessage): String =
        "Build with id = ${message.buildId} at stage ${message.stageId} has status ${message.buildStatus}"
}