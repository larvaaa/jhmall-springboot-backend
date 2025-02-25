package com.example.shopping

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.domain.AuditorAware

@SpringBootApplication
class ShoppingApplication

fun main(args: Array<String>) {
	runApplication<ShoppingApplication>(*args)

//	@Bean
//	fun auditorProvider(): AuditorAware<String> {
//		return () ->
//	}
}
