package com.build.stats.controller

import com.build.stats.dao.TagDao
import com.build.stats.model.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/tag/api")
class TagApiController @Autowired constructor(
    private val tagDao: TagDao
) {

    @PostMapping("/create")
    fun create(
        @RequestBody tag: Tag
    ): ResponseEntity<Tag> {
        val data = tagDao.save(tag)
        return ResponseEntity.ok(data)
    }
}