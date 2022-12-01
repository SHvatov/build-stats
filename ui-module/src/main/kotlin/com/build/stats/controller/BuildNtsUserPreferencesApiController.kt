package com.build.stats.controller

import com.build.stats.dao.BuildNtfUserPreferencesDao
import com.build.stats.model.BuildNtfUserConfig
import com.build.stats.model.BuildNtfUserPreferences
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/build/ntf/api")
class BuildNtsUserPreferencesApiController @Autowired constructor(
    private val buildNtfUserPreferencesDao: BuildNtfUserPreferencesDao
) {
    @PutMapping("/update/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody buildNtfUserConfig: BuildNtfUserConfig
    ): ResponseEntity<BuildNtfUserPreferences> {
        val buildNtfUserPreference = buildNtfUserPreferencesDao.findByIdOrNull(id)
        buildNtfUserPreference!!.config = buildNtfUserConfig
        val data = buildNtfUserPreferencesDao.save(buildNtfUserPreference)
        return ResponseEntity.ok(data)
    }

    @PostMapping("/create")
    fun create(
        @RequestBody buildNtfUserPreferences: BuildNtfUserPreferences
    ): ResponseEntity<BuildNtfUserPreferences> {
        val data = buildNtfUserPreferencesDao.save(buildNtfUserPreferences)
        return ResponseEntity.ok(data)
    }

}