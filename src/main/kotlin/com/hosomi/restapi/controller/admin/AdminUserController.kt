package com.hosomi.restapi.controller.admin

import com.hosomi.restapi.model.entity.UserInfo
import com.hosomi.restapi.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api/admin")
class AdminUserController {

    @Autowired
    lateinit var userService: UserService

    @RequestMapping(value = ["/users"], method = [(RequestMethod.GET)])
    fun getAllUsersInfo(request: HttpServletRequest): ResponseEntity<List<UserInfo>> =
        ResponseEntity.ok(userService.getAllUsers())
}