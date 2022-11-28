package com.build.stats.notification.manager.impl

import com.build.stats.config.StartupOrder
import com.build.stats.dao.NtfTypeDao
import com.build.stats.model.NtfType
import com.build.stats.notification.manager.NtfTypesStartupManager
import com.build.stats.notification.model.NtfTypeCode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component


@Component
class NtfTypesStartupManagerImpl @Autowired constructor(
    val ntfTypeDao: NtfTypeDao
) : NtfTypesStartupManager {

    @Order(StartupOrder.APPLICATION_LOADED)
    @EventListener(ContextRefreshedEvent::class)
    override fun updateNtfTypes() {
        NtfTypeCode.values().forEach {
            if (ntfTypeDao.findUniqueByType(it.name) == null) {
                ntfTypeDao.save(NtfType().apply {
                    this.type = it.name
                    this.description = it.description
                })
            }
        }
    }
}