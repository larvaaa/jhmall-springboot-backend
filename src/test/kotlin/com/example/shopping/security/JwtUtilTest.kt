package com.example.shopping.security

import io.jsonwebtoken.Claims
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class JwtUtilTest {

    @Autowired
    lateinit var jwtUtil: JwtUtil

    @Test
    fun generateToken() {
//        val accessToken = jwtUtil.generateAccessToken("123")
//        println("accessToken = ${accessToken}")
    }

    @Test
    fun validateToken() {

        val istokenValid = jwtUtil.validateToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjMiLCJpYXQiOjE3NDMzNDAwMTEsImV4cCI6MTc0MzM0MTgxMX0._8X-NSLJgKoCivdiCB5jo-v7dGCCUTCoMGhZ1B8AQFA")
        println("istokenValid = ${istokenValid}")

    }

    @Test
    fun getClaimsFromToken() {

        val claim: Claims? = jwtUtil.getClaimsFromToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjMiLCJpYXQiOjE3NDMzNDI2MjcsImV4cCI6MTc0MzM0NDQyN30.2K1NAHwtU_TfSmPoHoaUu760c2yEOkV7mlKmNVCyR-M")
        println("claim = ${claim}")
        println("iss = ${claim?.issuer}")
        println("aud = ${claim?.audience}")
        println("sub = ${claim?.subject}")
        println("iat = ${claim?.issuedAt}")
        println("exp = ${claim?.expiration}")

    }
}

