package com.hosomi.restapi.dao

import com.hosomi.restapi.model.entity.Role
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleDao : CrudRepository<Role, Long> {
    fun findByName(name: String): Role
}