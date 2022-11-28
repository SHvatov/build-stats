package com.build.stats.telegram.bot.service.impl

import com.build.stats.dao.UserDao
import com.build.stats.model.UserConfig
import com.build.stats.telegram.bot.service.TelegramRegistrationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.regex.Pattern


@Service
class TelegramRegistrationServiceImpl @Autowired constructor(
    private val userDao: UserDao
) : TelegramRegistrationService {

    override fun registerUser(chatId: String, email: String) {
        require(validateEmail(email)) {
            "Invalid email format! Please try again!"
        }

        val user = userDao.findByEmail(email)

        require(user != null) {
            "Invalid email! User not found! Please try again!"
        }

        user.config = UserConfig(chatId)

        userDao.save(user)
    }

    private fun validateEmail(email: String): Boolean {
        val matcher = EMAIL_VALIDATION_PATTERN.matcher(email)
        return matcher.find()
    }

    private companion object {
        const val EMAIL_VALIDATION_PATTERN_STR = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$"

        val EMAIL_VALIDATION_PATTERN = Pattern.compile(EMAIL_VALIDATION_PATTERN_STR, Pattern.CASE_INSENSITIVE)!!
    }
}