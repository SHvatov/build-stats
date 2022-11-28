package com.build.stats.telegram.bot.service


interface TelegramRegistrationService {
    fun registerUser(chatId: String, email: String)
}