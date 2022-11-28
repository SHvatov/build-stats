package com.build.stats.notification.service

import com.build.stats.model.User
import com.build.stats.notification.model.NtfTypeCode


interface NtfSender {
    val ntfTypeCode: NtfTypeCode

    fun send(notifyTo: String, notification: String)

    /**
     * Returns notification destination based on the config of the [User].
     */
    fun fetchNtfDestination(user: User): String?
}