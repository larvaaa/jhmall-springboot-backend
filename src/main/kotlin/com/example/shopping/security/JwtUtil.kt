package com.example.shopping.security

import io.jsonwebtoken.*
import io.jsonwebtoken.security.SignatureException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.spec.SecretKeySpec

@Component
class JwtUtil {

    private val log = LoggerFactory.getLogger(this.javaClass)!!

    @Value("\${jwt.secret-key}")
    lateinit private var SECRET_KEY: String

    @Value("\${jwt.access-token-expiration-time}")
    lateinit var ACCESS_TOKEN_EXPIRATION_TIME: String

    @Value("\${jwt.refresh-token-expiration-time}")
    lateinit var REFRESH_TOKEN_EXPIRATION_TIME: String

    fun generateAccessToken(memberId: String, roles: MutableList<String>?): String {
        val key = SecretKeySpec(SECRET_KEY.toByteArray(), SignatureAlgorithm.HS256.jcaName)
        val now = Date()
        val expirationDate = Date(now.time + ACCESS_TOKEN_EXPIRATION_TIME.toLong())

        return Jwts.builder()
            .claim("roles", roles)
            .setSubject(memberId)
            .setIssuedAt(now)
            .setExpiration(expirationDate)
            .signWith(key)
            .compact()
    }

    fun generateRefreshToken(memberId: String): String {
        val key = SecretKeySpec(SECRET_KEY.toByteArray(), SignatureAlgorithm.HS256.jcaName)
        val now = Date()
        val expirationDate = Date(now.time + REFRESH_TOKEN_EXPIRATION_TIME.toLong())

        return Jwts.builder()
            .setSubject(memberId)
            .setIssuedAt(now)
            .setExpiration(expirationDate)
            .signWith(key)
            .compact()
    }

    fun validateToken(token: String): Boolean {
        return try {
            val key = SecretKeySpec(SECRET_KEY.toByteArray(), SignatureAlgorithm.HS256.jcaName)
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
            true // 토큰이 유효함
        } catch (e: SignatureException) {
            log.error("Invalid JWT signature: ${e.message}")
            false
        } catch (e: MalformedJwtException) {
            log.error("Invalid JWT token format: ${e.message}")
            false
        } catch (e: ExpiredJwtException) {
            log.error("JWT token is expired: ${e.message}")
            false
        } catch (e: UnsupportedJwtException) {
            log.error("Unsupported JWT token: ${e.message}")
            false
        } catch (e: IllegalArgumentException) {
            log.error("JWT claims string is empty or invalid: ${e.message}")
            false
        } catch (e: Exception) {
            log.error("Unexpected error during JWT validation: ${e.message}")
            false
        }
    }

    fun getClaimsFromToken(token: String): Claims {
        val key = SecretKeySpec(SECRET_KEY.toByteArray(), SignatureAlgorithm.HS256.jcaName)
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body // 클레임 정보 반환
    }
}

