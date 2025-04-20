package com.example.shopping.repository

import com.example.shopping.domain.MemberAuthority
import org.springframework.data.jpa.repository.JpaRepository

interface MemberAuthorityRepository : JpaRepository<MemberAuthority, Long> {

    fun save(memberAuthority: MemberAuthority): MemberAuthority
}