package com.example.shopping.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**") // 모든 엔드포인트에 대해 CORS 설정
            .allowedOrigins("http://localhost:3000") // 허용된 출처
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 허용된 HTTP 메서드
            .allowedHeaders("*") // 허용된 헤더
            .allowCredentials(true) // 인증 정보 포함 허용
    }
}