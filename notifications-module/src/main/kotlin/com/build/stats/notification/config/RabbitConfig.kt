package com.build.stats.notification.config

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.FanoutExchange
import org.springframework.amqp.core.Queue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class RabbitConfig {

    @set:Autowired
    @set:Qualifier("ntfFanoutExchange")
    lateinit var fanoutExchange: FanoutExchange

    @Bean
    fun ntfQueue(): Queue = Queue("ntf-queue")

    @Bean
    fun ntfBinding(): Binding =
        BindingBuilder.bind(ntfQueue()).to(fanoutExchange)
}