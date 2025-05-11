package com.example.shopping.service

import com.example.shopping.repository.AuthorityRepository
import org.springframework.stereotype.Service

@Service
class AuthorityService(
    private val authorityRepository: AuthorityRepository
) {

    fun findRoles(): List<String> = authorityRepository.findRoles()

}