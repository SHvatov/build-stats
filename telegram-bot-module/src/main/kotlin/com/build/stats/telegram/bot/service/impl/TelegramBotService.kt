package com.build.stats.telegram.bot.service.impl

import com.build.stats.telegram.bot.service.TelegramRegistrationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update


@Service
class TelegramBotService @Autowired constructor(
    var telegramRegistrationService: TelegramRegistrationService
) : TelegramLongPollingBot() {

    @Value("\${bot.token}")
    private lateinit var token: String

    @Value("\${bot.username}")
    private lateinit var username: String

    override fun getBotToken() = token

    override fun getBotUsername() = username

    override fun onUpdateReceived(update: Update) {
        if (update.hasMessage()) {
            val message = update.message
            val response = SendMessage()
            val chatId = message.chatId.toString()
            response.chatId = chatId.toString()
            val text: String = if (message.text.equals(START_COMMAND)) {
                HELP_TEXT
            } else {
                try {
                    telegramRegistrationService.registerUser(chatId, message.text)
                    SUCCESS_TEXT
                } catch (e: IllegalArgumentException) {
                    e.message!!
                }
            }
            response.text = text
            execute(response)
        }
    }

    private companion object {
        const val START_COMMAND = "/start"
        const val HELP_TEXT = "Please enter your email"
        const val SUCCESS_TEXT = "You have been successfully subscribed to Telegram notifications!"
    }
}