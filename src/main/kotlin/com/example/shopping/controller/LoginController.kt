package com.example.shopping.controller

import com.example.shopping.domain.Member
import com.example.shopping.dto.MemberDto
import com.example.shopping.security.CustomUserDetailService
import com.example.shopping.security.JwtUtil
import com.example.shopping.service.AuthorityService
import com.example.shopping.service.MemberService
import io.jsonwebtoken.Claims
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

@RestController
class LoginController (
    private val authenticationManager: AuthenticationManager,
    val memberService: MemberService,
    val authorityService: AuthorityService,
    val passwordEncoder: PasswordEncoder,
    val jwtUtil: JwtUtil
) {

    private val log = LoggerFactory.getLogger(this.javaClass)!!

    @PostMapping("/login")
    fun login(
        @RequestBody loginRequest: LoginRequest,
        response: HttpServletResponse
    ): ResponseEntity<LoginResponse> {

        try {

            val authenticationRequest: UsernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.loginId, loginRequest.loginPw)

            val authenticationResponse: Authentication = authenticationManager.authenticate(authenticationRequest)
    //        SecurityContextHolder.getContext().authentication = authenticationResponse // 인증 상태 저장

            val findUserDetails: CustomUserDetailService.CustomUserDetails = authenticationResponse.principal as CustomUserDetailService.CustomUserDetails
            val memberId: String = findUserDetails.id.toString()
            val roles: MutableList<String>? = findUserDetails.roles
            val memberName: String = findUserDetails.name

            val accessToken: String = jwtUtil.generateAccessToken(memberId, roles)
            log.info("accessToken = {}", accessToken)
            val accessCookie: ResponseCookie = ResponseCookie.from("accessToken", accessToken)
                .domain("localhost")
                .path("/")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .build()

            val refreshToken: String = jwtUtil.generateRefreshToken(memberId)
    //        val encodeToken: String = Base64.getUrlEncoder().encodeToString(refreshToken.toByteArray())

            val refreshCookie: ResponseCookie = ResponseCookie.from("refreshToken", refreshToken)
                .domain("localhost")
                .path("/")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .build()

            response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString())
            response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString())

            return ResponseEntity.ok(LoginResponse(accessToken = accessToken, memberId = memberId.toLong(), memberName = memberName, isLogin = true))
        } catch (e: BadCredentialsException) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
    }

    @GetMapping("/login")
    fun login(
        @CookieValue("accessToken", required = false) accessToken: String?
    ): ResponseEntity<LoginResponse> {

        log.info("accessToken of cookie ====> ${accessToken}")

        if (accessToken == null) {
            return ResponseEntity.ok(LoginResponse(accessToken = null, memberId = null, memberName = null, isLogin = false))
        }

        val isAccessTokenValid: Boolean = jwtUtil.validateToken(accessToken)
        if(!isAccessTokenValid) throw AccessDeniedException("토큰 무결성 검증 실패")
        val claim: Claims = jwtUtil.getClaimsFromToken(accessToken)
        val memberId: Long = claim.subject.toLong()

        val findMember: MemberDto = memberService.findById(memberId)
        val memberName: String = findMember.name!!

        return ResponseEntity.ok(LoginResponse(accessToken = accessToken, memberId = memberId, memberName = memberName, isLogin = true))
    }

    @PostMapping("/users")
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

    @GetMapping("/users/{id}")
    fun getUserInfo(@PathVariable("id") id: Long): ResponseEntity<MemberDto> {

        val dto: MemberDto = memberService.findById(id)
        return ResponseEntity.ok(dto)
    }

    @GetMapping("/duplicateCheck")
    fun duplicateCheck(@RequestParam loginId: String): ResponseEntity<String> {
        val checkResult = memberService.duplicateCheck(loginId)
        return ResponseEntity.ok(checkResult)
    }

    @GetMapping("/roles")
    fun findRoles(): ResponseEntity<List<String>> {

        val roles: List<String> = authorityService.findRoles()
        return ResponseEntity.ok(roles)

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
        val accessToken: String?,
        val memberId: Long?,
        val memberName: String?,
        val isLogin: Boolean
    )

}