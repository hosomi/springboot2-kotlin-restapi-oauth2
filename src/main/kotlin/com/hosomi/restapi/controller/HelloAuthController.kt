package com.hosomi.restapi.controller

import com.hosomi.restapi.model.HelloResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class HelloAuthController {
    @RequestMapping(value = ["/public/hello"], method = [(RequestMethod.GET)])
    fun getHelloMessage(): ResponseEntity<String> = ResponseEntity.ok("Hello!")

    @RequestMapping(value = ["/user/hello/{name}"], method = [(RequestMethod.GET)])
    fun getHelloMessageWithName(@PathVariable("name") name: String):
            ResponseEntity<Any> = ResponseEntity.ok(HelloResponse(name = name, message = "Hello $name !"))
}