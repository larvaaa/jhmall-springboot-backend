package com.example.shopping.security

import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    val jwtUtil: JwtUtil
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {

        val allowedPaths = arrayOf("/login", "/signUp", "/healthCheck", "/error")

        http {
//            securityMatcher("/auth/**")
            csrf { disable() }
            formLogin { disable() }
            httpBasic { disable() }
            sessionManagement {
                sessionCreationPolicy = SessionCreationPolicy.STATELESS
            }
            authorizeHttpRequests {
                allowedPaths.forEach { authorize(it, permitAll) }
                authorize(HttpMethod.OPTIONS, "/**", permitAll)
//                authorize("/login", permitAll)
//                authorize("/admin", hasAuthority("ADMIN"))
                authorize(anyRequest, authenticated)
            }

//            exceptionHandling {
//                it.accessDeniedHandler { request, response, accessDeniedException ->
//                    response.status = HttpServletResponse.SC_FORBIDDEN
//                    response.contentType = MediaType.APPLICATION_JSON_VALUE
//                    response.writer.write(
//                        """{"error": "Access denied", "message": "${accessDeniedException.message}", "path": "${request.servletPath}"}"""
//                    )
//                }
//            }
            // 커스텀 필터를 추가
            http.addFilterBefore(CustomTokenAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter::class.java)
        }

        return http.build()
    }

    @Bean
    fun authenticationManager(
        userDetailsService: CustomUserDetailService,
        passwordEncoder: PasswordEncoder
    ): AuthenticationManager {
        val authenticationProvider = DaoAuthenticationProvider()
        authenticationProvider.setUserDetailsService(userDetailsService)
        authenticationProvider.setPasswordEncoder(passwordEncoder)

        // ProviderManager가 AuthenticationManager를 구현
        return ProviderManager(authenticationProvider)
    }

//    @Bean
//    fun dataSource(): DataSource {
//        return EmbeddedDatabaseBuilder()
//            .setType(H2)
//            .addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)
//            .build()
//    }

//    @Bean
//    fun users(dataSource: DataSource): UserDetailsManager {
//        val user = User.builder()
//            .username("jungho")
//            .password("{bcrypt}$2a$10\$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW")
//            .roles("USER")
//            .build();
//        val users = JdbcUserDetailsManager(dataSource)
//        users.createUser(user)
//        return users
//    }

//    @Bean
//    fun userDetailsService(): UserDetailsService {
//        val user = User.builder()
//            .username("bjh55")
//            .password("{noop}55132")
//            .roles("USER")
//            .build()
//
//        return InMemoryUserDetailsManager(user)
//    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }
}