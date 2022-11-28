package com.build.stats.notification.service.impl

import com.build.stats.model.User
import com.build.stats.notification.model.NtfTypeCode
import com.build.stats.notification.service.NtfSender
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Component


@Component("mailNtfSender")
class MailNtfSender @Autowired constructor(
    val mailSender: JavaMailSender
) : NtfSender {

    @Value("\${spring.mail.username}")
    private lateinit var username: String

    override val ntfTypeCode: NtfTypeCode = NtfTypeCode.MAIL

    override fun send(notifyTo: String, notification: String) {
        val mailMessage = SimpleMailMessage()

        mailMessage.setFrom(username)
        mailMessage.setTo(notifyTo)
        mailMessage.setSubject(SUBJECT)
        mailMessage.setText(notification)

        mailSender.send(mailMessage)
    }

    override fun fetchNtfDestination(user: User): String? = user.email

    private companion object {
        const val SUBJECT = "GitHub build status"
    }
}