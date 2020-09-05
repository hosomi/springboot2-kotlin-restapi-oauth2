package com.hosomi.restapi.dao

import com.hosomi.restapi.model.entity.UserInfo
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserInfoDao : CrudRepository<UserInfo, Long> {
    fun findOneByUserId(userId: Long): UserInfo?
}
