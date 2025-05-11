package com.example.shopping.repository

import com.example.shopping.domain.QAuthority.*
import com.querydsl.jpa.impl.JPAQueryFactory

class AuthorityRepositoryImpl(
    private val jPAQueryFactory: JPAQueryFactory
) : AuthorityRepositoryCustom {

    override fun findRoles(): List<String> {
        return jPAQueryFactory
            .select(authority.role)
            .from(authority)
            .fetch()
    }
}