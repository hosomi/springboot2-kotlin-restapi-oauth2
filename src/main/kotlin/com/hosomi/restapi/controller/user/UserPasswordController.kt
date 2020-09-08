package com.hosomi.restapi.controller.user

import com.hosomi.restapi.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api/user")
class UserPasswordController {

    @Autowired
    lateinit var userService: UserService

    @RequestMapping(value = ["/password/change"], method = [(RequestMethod.POST)])
    fun passwordChange(
        @RequestParam("newPassword") newPassword: String,
        @RequestParam("oldPassword") oldPassword: String,
        request: HttpServletRequest
    ): ResponseEntity<Unit> =
        if (userService.updatePassword(request.userPrincipal.name, oldPassword, newPassword)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.badRequest().build()
        }
}