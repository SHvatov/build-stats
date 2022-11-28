package com.build.stats.notification.service.impl

import com.build.stats.model.User
import com.build.stats.notification.model.NtfTypeCode
import com.build.stats.notification.service.NtfSender
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.stereotype.Component



@Component("telegramNtfSender")
class TelegramNtfSender @Autowired constructor(
    val restTemplateBuilder: RestTemplateBuilder
) : NtfSender {

    @Value("\${bot.token}")
    private lateinit var token: String

    override val ntfTypeCode: NtfTypeCode = NtfTypeCode.TELEGRAM

    override fun send(notifyTo: String, notification: String) {
        val restTemplate = restTemplateBuilder.build()
        val response = restTemplate.getForEntity(URI, String::class.java, token, notifyTo, notification)
        require(response.statusCodeValue == 200) {
            "Invalid request to telegram bot!"
        }
    }

    override fun fetchNtfDestination(user: User): String? =
        user.config.chatId

    private companion object {
        const val URI = "https://api.telegram.org/bot{token}/sendMessage?chat_id={chatId}&text={text}"
    }
}