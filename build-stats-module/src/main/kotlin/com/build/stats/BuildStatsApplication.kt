package com.build.stats

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BuildStatsApplication

fun main(args: Array<String>) {
    runApplication<BuildStatsApplication>(*args)
}