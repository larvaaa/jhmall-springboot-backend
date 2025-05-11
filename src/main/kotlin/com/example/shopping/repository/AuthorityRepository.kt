package com.example.shopping.repository

import com.example.shopping.domain.Authority
import org.springframework.data.jpa.repository.JpaRepository

interface AuthorityRepository : JpaRepository<Authority, Long>, AuthorityRepositoryCustom {

    fun findByRole(role: String): Authority

}