package com.hosomi.restapi.controller.user

import com.hosomi.restapi.model.entity.UserInfo
import com.hosomi.restapi.service.UserService
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.security.Principal

@WebMvcTest(UserMyController::class)
@AutoConfigureMockMvc(addFilters = false)
internal class UserMyControllerTest {

    @MockkBean
    lateinit var mockUserService: UserService

    @Autowired
    private lateinit var mockMvc: MockMvc

    private val username = "hosomi@example.com"

    private val mockPrincipal = mockk<Principal> {
        every { name } returns username
    }

    @Test
    fun `getting user information`() {

        val expectedUserInfo = UserInfo(id = 300L, userId = 4L, email = "hosomi@example.com", profileImage = "url")
        val objectMapper = ObjectMapper()
        val userInfoJSON = objectMapper.writeValueAsString(expectedUserInfo)

        every { mockUserService.getUserInfo(username) } returns expectedUserInfo

        my()
            .andExpect(status().isOk)
            .andExpect(content().json(userInfoJSON))
            .andReturn()

        verify { mockUserService.getUserInfo(username) }
    }

    @Test
    fun `nonexistent user information`() {

        every { mockUserService.getUserInfo(username) } returns null

        my()
            .andExpect(status().isNotFound)
            .andReturn()

        every { mockUserService.getUserInfo(username) }
    }

    private fun my() = mockMvc.perform(MockMvcRequestBuilders.get("/api/user/my")
        .principal(mockPrincipal)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
    )
}