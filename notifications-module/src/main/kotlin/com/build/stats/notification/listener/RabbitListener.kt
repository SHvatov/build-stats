package com.build.stats.notification.listener

import com.build.stats.notification.service.BuildNotificationService
import com.build.stats.vo.mq.BuildUpdateNtfMessage
import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
@EnableRabbit
class RabbitListener @Autowired constructor(
    val buildNotificationService: BuildNotificationService
) {
    @RabbitListener(queues = ["ntf-queue"])
    fun processNtfQueue(message: BuildUpdateNtfMessage) {
        buildNotificationService.sendNotificationsToDestinations(message)
    }
}