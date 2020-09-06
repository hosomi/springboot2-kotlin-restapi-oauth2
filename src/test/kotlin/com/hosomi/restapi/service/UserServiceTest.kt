package com.hosomi.restapi.service

import com.hosomi.restapi.dao.RoleDao
import com.hosomi.restapi.dao.UserDao
import com.hosomi.restapi.dao.UserInfoDao
import com.hosomi.restapi.model.entity.Role
import com.hosomi.restapi.model.entity.User
import com.hosomi.restapi.model.entity.UserInfo
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder

@WebMvcTest(UserService::class)
@AutoConfigureMockMvc(addFilters = false)
internal class UserServiceTest {

    @MockkBean
    lateinit var mockUserDao: UserDao

    @MockkBean
    lateinit var mockRoleDao: RoleDao

    @MockkBean
    lateinit var mockUserInfoDao: UserInfoDao

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var service: UserService

    private val username = "hosomi@example.com"

    @Test
    fun `basic findOneByUsername test`() {

        val expectedUser = mockk<User>(relaxed = true)
        every { mockUserDao.findOneByUsername(username) } returns expectedUser

        val userDetails = service.loadUserByUsername(username)

        verify { mockUserDao.findOneByUsername(username) }
        assertEquals(expectedUser.username, userDetails.username)
    }

    @Test
    fun `basic loadUserByUsername test`() {
        every { mockUserDao.findOneByUsername(username) } returns null

        assertThrows<UsernameNotFoundException> {
            service.loadUserByUsername(username)
        }

        verify { mockUserDao.findOneByUsername(username) }
    }

    @Test
    fun `new user registration to return userinfo`() {

        val userId = 3L
        val expectedUser = mockk<User>(relaxed = true) {
            every { id } returns userId
        }

        val userRole = mockk<Role>(relaxed = true)
        val expectedNewUserInfo = UserInfo(userId = userId, email = username)
        every { mockUserDao.findOneByUsername(username) } returns null
        every { mockRoleDao.findByName("USER") } returns userRole
        every { mockUserDao.save(any<User>()) } returns expectedUser
        every { mockUserInfoDao.save(expectedNewUserInfo) } returns expectedNewUserInfo

        val newUser = service.createUser(username, "password")
        verify { mockUserDao.findOneByUsername(username) }
        verify { mockRoleDao.findByName("USER") }
        verify { mockUserDao.save(any<User>()) }
        verify { mockUserInfoDao.save(expectedNewUserInfo) }
        assertEquals(expectedUser, newUser)
    }

    @Test
    fun `duplicate user registration to return null`() {

        val sameEmailUser = mockk<User>(relaxed = true)
        every { mockUserDao.findOneByUsername(username) } returns sameEmailUser

        val newUser = service.createUser(username, "password")

        verify { mockUserDao.findOneByUsername(username) }
        assertNull(newUser)
    }

    @Test
    fun `change password success to return true`() {

        val oldPassword = "1234"
        val newPassword = "newPassword"
        val expectedUser = mockk<User>(relaxed = true) {
            every { password } returns passwordEncoder.encode(oldPassword)
        }
        every { mockUserDao.findOneByUsername(username) } returns expectedUser
        every { mockUserDao.save(any<User>()) } returns expectedUser

        val passwordUpdated = service.updatePassword(username, oldPassword, newPassword)

        verify { mockUserDao.findOneByUsername(username) }
        verify { mockUserDao.save(any<User>()) }
        assertTrue(passwordUpdated)
    }

    @Test
    fun `returns false on password change failure`() {
        val oldPassword = "wrongOldPassword"
        val newPassword = "newPassword"
        val expectedUser = mockk<User>(relaxed = true) {
            every { password } returns passwordEncoder.encode("oldPassword")
        }
        every { mockUserDao.findOneByUsername(username) } returns expectedUser

        val passwordUpdated = service.updatePassword(username, oldPassword, newPassword)

        verify { mockUserDao.findOneByUsername(username) }
        assertTrue(passwordUpdated.not())
    }

    @Test
    fun `getting user information`() {

        val userId = 3L
        val expectedUser = mockk<User>(relaxed = true) {
            every { id } returns userId
        }

        val expectedUserInfo = mockk<UserInfo>(relaxed = true)
        every { mockUserDao.findOneByUsername(username) } returns expectedUser
        every { mockUserInfoDao.findOneByUserId(userId) } returns expectedUserInfo

        val userInfo = service.getUserInfo(username)
        verify { mockUserDao.findOneByUsername(username) }
        verify { mockUserInfoDao.findOneByUserId(userId) }
        assertEquals(expectedUserInfo, userInfo)
    }

    @Test
    fun `nonexistent user information`() {

        every { mockUserDao.findOneByUsername(username) } returns null

        val userInfo = service.getUserInfo(username)
        verify { mockUserDao.findOneByUsername(username) }
        assertNull(userInfo)
    }

    @Test
    fun `getting user list information`() {

        val expectedUsers = listOf<UserInfo>(mockk(), mockk())
        every { mockUserInfoDao.findAll() } returns expectedUsers

        val users = service.getAllUsers()
        verify { mockUserInfoDao.findAll() }
        assertEquals(expectedUsers, users)
    }
}