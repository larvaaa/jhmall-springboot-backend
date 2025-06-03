package com.example.shopping.security

import io.jsonwebtoken.Claims
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.tomcat.util.http.parser.Cookie
import org.slf4j.LoggerFactory
import org.springframework.security.access.AccessDeniedException
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
        AntPathRequestMatcher("/error"),
        AntPathRequestMatcher("/duplicateCheck"),
    )

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        val servletPath = request.servletPath ?: ""
        log.info("Processing path: $servletPath")

        if (skipPaths.any { it.matches(request) } || request.method == "OPTIONS") {
            log.info("Skipping authentication for path: $servletPath")
            SecurityContextHolder.clearContext() // SecurityContext 초기화
            filterChain.doFilter(request, response)
            return
        }

//        val headerNames = request.headerNames
//        println(" 헤더 목록 ===========>")
//        while (headerNames.hasMoreElements()) {
//            println(headerNames.nextElement())
//        }

//        val cookies = request.cookies
//        println(" 쿠키 목록 ===========>")
//        for ((index, value) in cookies.withIndex()) {
//            println("cookie[${index}] = $value")
//        }

        val authorizationHeaderValue = request.getHeader("authorization") ?: throw NullPointerException("Authorization 헤더가 없습니다.")

        if(!authorizationHeaderValue.startsWith("Bearer ")) throw IllegalStateException("Bearer 토큰 형식이 맞지 않습니다.")

        val accessToken: String = authorizationHeaderValue.substring(7)
        log.info("accessToken = $accessToken")

        val isAccessTokenValid: Boolean = jwtUtil.validateToken(accessToken)

        if(!isAccessTokenValid) throw AccessDeniedException("토큰 무결성 검증 실패")

        val claim: Claims = jwtUtil.getClaimsFromToken(accessToken)

        log.info("sub = ${claim.subject}")
        val memberId: Long = claim.subject.toLong()
        val roles: MutableList<GrantedAuthority> = claim.get("roles") as MutableList<GrantedAuthority>
//        val roles = (claim["roles"] as? List<*>)?.map { SimpleGrantedAuthority(it.toString()) } ?: emptyList()

        val authentication: Authentication = UsernamePasswordAuthenticationToken(memberId, null, roles)

        SecurityContextHolder.getContext().authentication = authentication

        filterChain.doFilter(request, response)

    }
}