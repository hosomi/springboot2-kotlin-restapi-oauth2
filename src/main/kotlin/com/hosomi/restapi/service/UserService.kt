package com.hosomi.restapi.service

import com.hosomi.restapi.dao.RoleDao
import com.hosomi.restapi.dao.UserDao
import com.hosomi.restapi.dao.UserInfoDao
import com.hosomi.restapi.model.entity.User
import com.hosomi.restapi.model.entity.UserInfo

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service("userDetailsService")
class UserService  : UserDetailsService {

    @Autowired
    private lateinit var userDao: UserDao

    @Autowired
    private lateinit var roleDao: RoleDao

    @Autowired
    private lateinit var userInfoDao: UserInfoDao

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails =
            userDao.findOneByUsername(username)
                    ?: throw UsernameNotFoundException("ユーザが存在しません。")

    fun createUser(email: String, password: String): User? =
            if (userDao.findOneByUsername(email) == null) {
                val newUser = User()
                newUser.username = email
                newUser.password = passwordEncoder.encode(password)
                newUser.setRoles(listOf(roleDao.findByName("USER")))
                val userSaved = userDao.save(newUser)
                userInfoDao.save(
                        UserInfo(
                                userId = userSaved.id,
                                email = email
                        )
                )
                userSaved
            } else null

    fun updatePassword(email: String, oldPassword: String, newPassword: String): Boolean {
        val user = userDao.findOneByUsername(email)!!
        return if (passwordEncoder.matches(oldPassword, user.password)) {
            user.password = passwordEncoder.encode(newPassword)
            userDao.save(user)
            true
        } else {
            false
        }
    }

    fun getUserInfo(email: String): UserInfo? =
            userDao.findOneByUsername(email)?.let {
                userInfoDao.findOneByUserId(it.id)
            }

    fun getAllUsers(): List<UserInfo> =
            userInfoDao.findAll().toList()
}