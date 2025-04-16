package com.example.shopping.security

import io.jsonwebtoken.Claims
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.web.filter.OncePerRequestFilter

class CustomTokenAuthFilter(
    val jwtUtil: JwtUtil
) : OncePerRequestFilter() {

    private val log = LoggerFactory.getLogger(this.javaClass)!!

    private val skipPaths = listOf(
        AntPathRequestMatcher("/login"),
        AntPathRequestMatcher("/signUp"),
        AntPathRequestMatcher("/healthCheck")
    )

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        if (skipPaths.any { it.matches(request) } || request.method == "OPTIONS") {
            filterChain.doFilter(request, response)
            return
        }

//        val headerNames = request.headerNames
//        while (headerNames.hasMoreElements()) {
//            println(headerNames.nextElement())
//        }

        val AuthorizationHeaderValue = request.getHeader("authorization")

        if(AuthorizationHeaderValue == null) throw NullPointerException("Authorization 헤더가 없습니다.")
        if(!AuthorizationHeaderValue.startsWith("Bearer ")) throw IllegalStateException("Bearer 토큰 형식이 맞지 않습니다.")

        val accessToken: String = AuthorizationHeaderValue.substring(7)
        log.info("accessToken = ${accessToken}")

        val isAccessTokenValid: Boolean = jwtUtil.validateToken(accessToken)

        if(!isAccessTokenValid) throw Exception("토큰 무결성 검증 실패")

        val claim: Claims = jwtUtil.getClaimsFromToken(accessToken)

        log.info("sub = ${claim.subject}")
        val memberId: Long = claim.subject.toLong()
        val roles: MutableList<GrantedAuthority> = claim.get("roles") as MutableList<GrantedAuthority>

        val authentication: Authentication = UsernamePasswordAuthenticationToken(memberId, null, roles)

        SecurityContextHolder.getContext().authentication = authentication

        filterChain.doFilter(request, response)

    }
}