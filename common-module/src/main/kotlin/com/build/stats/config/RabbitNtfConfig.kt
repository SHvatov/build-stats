package com.build.stats.config

import org.springframework.amqp.core.FanoutExchange
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class RabbitNtfConfig {
    @field:Value("\${app.ntf-exchange}")
    lateinit var ntfExchange: String

    @Bean
    fun ntfFanoutExchange(): FanoutExchange =
        FanoutExchange(ntfExchange)

    @Bean
    fun ntfRabbitTemplate(connectionFactory: ConnectionFactory): RabbitTemplate =
        RabbitTemplate(connectionFactory).apply {
            setExchange(ntfExchange)
        }
}