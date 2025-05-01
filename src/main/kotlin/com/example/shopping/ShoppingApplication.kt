package com.example.shopping

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ShoppingApplication

fun main(args: Array<String>) {
	runApplication<ShoppingApplication>(*args)

//	@Bean
//	fun auditorProvider(): AuditorAware<String> {
//		return () ->
//	}

//	@Bean
//	fun objectMapper(): ObjectMapper {
//		val mapper = ObjectMapper()
//		val hibernateModule = Hibernate5Module()
//			.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, false)
//			.configure(Hibernate5Module.Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS, true)
//		mapper.registerModule(hibernateModule)
//		return mapper
//	}
}
