package com.example.shopping.controller

import com.example.shopping.domain.Member
import com.example.shopping.security.CustomUserDetailService
import com.example.shopping.security.JwtUtil
import com.example.shopping.service.MemberService
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class LoginController (
    val authenticationManager: AuthenticationManager,
    val memberService: MemberService,
    val passwordEncoder: PasswordEncoder,
    val jwtUtil: JwtUtil
) {

    private val log = LoggerFactory.getLogger(this.javaClass)!!

    @PostMapping("/login")
    fun login(
        @RequestBody loginRequest: LoginRequest,
        response: HttpServletResponse
    ): ResponseEntity<LoginResponse> {

        log.info("start login controller")

        val authenticationRequest: UsernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.loginId, loginRequest.loginPw)

        val authenticationResponse: Authentication = authenticationManager.authenticate(authenticationRequest)
//        SecurityContextHolder.getContext().authentication = authenticationResponse // 인증 상태 저장

        val findUserDetails: CustomUserDetailService.CustomUserDetails = authenticationResponse.principal as CustomUserDetailService.CustomUserDetails
        val memberId: String = findUserDetails.id.toString()
        val roles: MutableList<String>? = findUserDetails.roles

        val accessToken: String = jwtUtil.generateAccessToken(memberId, roles)
        log.info("accessToken = {}", accessToken)

        val refreshToken: String = jwtUtil.generateRefreshToken(memberId)
//        val encodeToken: String = Base64.getUrlEncoder().encodeToString(refreshToken.toByteArray())

        val cookie: ResponseCookie = ResponseCookie.from("refreshToken", refreshToken)
            .maxAge(14 * 24 * 60 * 60)
            .domain("localhost")
            .httpOnly(true)
            .secure(true)
            .sameSite("None")
            .build()

        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString())

        return ResponseEntity.ok(LoginResponse(accessToken, "Bearer"))
    }

    @PostMapping("/signUp")
    fun signUp(@RequestBody signUpRequest: SignUpRequest): ResponseEntity<Member> {

        val newMember = Member(
            loginId = signUpRequest.loginId,
            loginPw = passwordEncoder.encode(signUpRequest.loginPw),
            name = signUpRequest.name,
            mobileNumber = signUpRequest.mobileNumber
        )

        memberService.register(newMember, "USER")

        return ResponseEntity.ok(newMember)

    }

    data class SignUpRequest(
        val loginId: String,
        val loginPw: String,
        val name: String,
        val mobileNumber: String
    )

    data class LoginRequest(
        val loginId: String,
        val loginPw: String
    )

    data class LoginResponse(
        val accessToken: String,
        val tokenType: String
    )

}