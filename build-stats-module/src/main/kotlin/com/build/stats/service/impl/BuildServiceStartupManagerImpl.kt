package com.build.stats.service.impl

import com.build.stats.config.StartupOrder
import com.build.stats.dao.BuildDao
import com.build.stats.model.BuildStatus
import com.build.stats.service.BuildServiceStartupManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Service


@Service
class BuildServiceStartupManagerImpl @Autowired constructor(
    private val buildDao: BuildDao
) : BuildServiceStartupManager {
    @Order(StartupOrder.APPLICATION_LOADED)
    @EventListener(ContextRefreshedEvent::class)
    override fun updateUntrackedBuilds() {
        buildDao.findAllByStatus(BuildStatus.IN_PROGRESS)
            .forEach {
                it.status = BuildStatus.APP_ERROR
                buildDao.save(it)
            }
    }
}