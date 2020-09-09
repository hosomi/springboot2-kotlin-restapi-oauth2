package com.hosomi.restapi.controller.admin

import com.fasterxml.jackson.databind.ObjectMapper
import com.hosomi.restapi.model.entity.UserInfo
import com.hosomi.restapi.service.UserService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(AdminUserController::class)
@AutoConfigureMockMvc(addFilters = false)
class AdminUserControllerTest {

    @MockkBean
    lateinit var mockUserService: UserService

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `getting user information list`() {

        val users = listOf(
                UserInfo(id = 1L, userId = 3L, email = "admin@example.com", profileImage = "url"),
                UserInfo(id = 2L, userId = 4L, email = "hosomi@example.com", profileImage = "url2")
        )

        val objectMapper = ObjectMapper()
        val userInfoJSON = objectMapper.writeValueAsString(users)
        every { mockUserService.getAllUsers() } returns users

        adminUsers()
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json(userInfoJSON))
            .andReturn()

        verify { mockUserService.getAllUsers() }
    }

    private fun adminUsers() =
        mockMvc.perform(MockMvcRequestBuilders.get("/api/admin/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        )
}