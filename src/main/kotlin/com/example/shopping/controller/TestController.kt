package com.example.shopping.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {

    @GetMapping("/aaa")
    fun test(): String {
        return "hello world"
    }
}