package com.example.shopping.repository

import com.example.shopping.domain.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long>, MemberRepositoryCustom {

    fun findByLoginId(username: String): Member

    fun save(member: Member): Member
}