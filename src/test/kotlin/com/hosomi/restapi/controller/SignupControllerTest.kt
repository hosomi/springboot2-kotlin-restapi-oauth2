package com.hosomi.restapi.controller


import com.hosomi.restapi.controller.SignupController
import com.hosomi.restapi.model.entity.User
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(SignupController::class)
@AutoConfigureMockMvc(addFilters = false)
internal class SignupControllerTest {

    @MockkBean
    lateinit var mockUserService: UserService

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `new user registration`() {

        val email = "hosomi2@example.com"
        val password = "p@ssword1234!"
        val newUserId = 100L
        val expectedUser = mockk<User> {
            every { id } returns newUserId
        }

        every { mockUserService.createUser(email, password) } returns expectedUser

        signUp(email, password)
            .andExpect(status().isCreated)
            .andExpect(content().string(newUserId.toString()))
            .andReturn()

        verify { mockUserService.createUser(email, password) }
    }

    @Test
    fun `duplicate user registration`() {

        val email = "newuser@example.com"
        val password = "p@ssword1234!"

        every { mockUserService.createUser(email, password) } returns null

        signUp(email, password)
            .andExpect(status().isConflict)
            .andReturn()

        verify { mockUserService.createUser(email, password) }
    }

    private fun signUp(email: String, password: String) = mockMvc.perform(
        MockMvcRequestBuilders.post("/api/signup")
            .param("email", email)
            .param("password", password)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
    )
}