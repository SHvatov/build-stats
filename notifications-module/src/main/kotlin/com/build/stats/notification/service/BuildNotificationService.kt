package com.build.stats.notification.service

import com.build.stats.vo.mq.BuildUpdateNtfMessage


interface BuildNotificationService {
    fun sendNotificationsToDestinations(message: BuildUpdateNtfMessage)
}