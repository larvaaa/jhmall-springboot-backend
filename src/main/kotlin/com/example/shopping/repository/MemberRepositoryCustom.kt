package com.example.shopping.repository

import com.example.shopping.dto.MemberDto

interface MemberRepositoryCustom {

    fun findMemberAndAuthority(username: String): MemberDto

}