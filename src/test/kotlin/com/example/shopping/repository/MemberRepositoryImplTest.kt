package com.example.shopping.repository

import com.example.shopping.dto.MemberDto
import com.example.shopping.security.JwtUtil
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class MemberRepositoryImplTest {

    @Autowired
    lateinit var memberRepositoryImpl: MemberRepositoryImpl

    @Test
    fun test() {
        val memberDto: MemberDto = memberRepositoryImpl.findMemberAndAuthority("user01")
        println(memberDto)
    }

}