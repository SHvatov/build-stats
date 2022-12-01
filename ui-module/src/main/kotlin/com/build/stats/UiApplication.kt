package com.build.stats

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class UIApplication

fun main(args: Array<String>) {
    runApplication<UIApplication>(*args)
}