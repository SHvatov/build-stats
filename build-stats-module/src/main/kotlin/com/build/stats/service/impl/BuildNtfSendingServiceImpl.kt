package com.build.stats.service.impl

import com.build.stats.model.BuildStatus
import com.build.stats.service.BuildNtfSendingService
import com.build.stats.vo.mq.BuildUpdateNtfMessage
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service


@Service
class BuildNtfSendingServiceImpl @Autowired constructor(
    @Qualifier("ntfRabbitTemplate") private val rabbitTemplate: RabbitTemplate,
) : BuildNtfSendingService {
    override fun sendBuildUpdateMessageToQueue(buildId: Long, status: BuildStatus, stageId: Long?) {
        rabbitTemplate.convertAndSend(BuildUpdateNtfMessage(buildId, status, stageId))
    }
}