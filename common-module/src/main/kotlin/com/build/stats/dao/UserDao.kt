package com.build.stats.dao

import com.build.stats.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository


@Repository
interface UserDao : JpaRepository<User, Long> {
    /**
     * Finds entity by provided [email].
     */
    @Query(value = "select e from User e where e._email = ?1")
    fun findByEmail(email: String): User?
}