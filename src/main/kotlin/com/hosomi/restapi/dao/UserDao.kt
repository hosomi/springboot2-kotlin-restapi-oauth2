package com.hosomi.restapi.dao

import com.hosomi.restapi.model.entity.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserDao : CrudRepository<User, Long> {
    fun findOneByUsername(email: String): User?
}