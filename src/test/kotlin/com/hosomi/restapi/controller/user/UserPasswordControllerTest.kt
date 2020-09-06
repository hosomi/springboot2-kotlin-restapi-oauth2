package com.hosomi.restapi.controller.user

import com.hosomi.restapi.service.UserService
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.security.Principal

@WebMvcTest(UserPasswordController::class)
@AutoConfigureMockMvc(addFilters = false)
class UserPasswordControllerTest {

    @MockkBean
    lateinit var mockUserService: UserService

    @Autowired
    private lateinit var mockMvc: MockMvc

    private val username = "hosomi@example.com"

    private val mockPrincipal = mockk<Principal> {
        every { name } returns username
    }

    @Test
    fun `password changed success`() {

        val newPassword = "12345"
        val oldPassword = "48574"

        every { mockUserService.updatePassword(username, oldPassword, newPassword) } returns true

        passwordChange(newPassword, oldPassword)
            .andExpect(status().isNoContent)
            .andReturn()

        verify { mockUserService.updatePassword(username, oldPassword, newPassword) }
    }

    @Test
    fun `current password mismatch failure`() {

        val newPassword = "123456"
        val oldPassword = "574874"

        every { mockUserService.updatePassword(username, oldPassword, newPassword) } returns false

        passwordChange(newPassword, oldPassword)
            .andExpect(status().isBadRequest)
            .andReturn()

        every { mockUserService.updatePassword(username, oldPassword, newPassword) }
    }

    private fun passwordChange(newPassword: String, oldPassword: String) =
        mockMvc.perform(post("/api/user/password/change")
            .param("newPassword", newPassword)
            .param("oldPassword", oldPassword)
            .principal(mockPrincipal)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        )
}