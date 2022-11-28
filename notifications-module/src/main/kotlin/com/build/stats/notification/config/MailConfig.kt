package com.build.stats.notification.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl



@Configuration
class MailConfig {
    @Value("\${spring.mail.host}")
    private val host: String? = null

    @Value("\${spring.mail.username}")
    private val username: String? = null

    @Value("\${spring.mail.password}")
    private val password: String? = null

    @Value("\${spring.mail.port}")
    private val port = 0

    @Value("\${spring.mail.protocol}")
    private val protocol: String? = null

    @Bean
    fun getMailSender(): JavaMailSender {
        val mailSender = JavaMailSenderImpl().apply {
            this.host = host
            this.port = port
            this.username = username
            this.password = password
        }
        val properties = mailSender.javaMailProperties
        properties["mail.transport.protocol"] = protocol
        return mailSender
    }
}