package com.example.shopping.repository

import com.example.shopping.domain.QMember.member
import com.example.shopping.domain.QMemberAuthority.memberAuthority
import com.example.shopping.dto.MemberDto
import com.example.shopping.dto.QMemberDto
import com.querydsl.jpa.impl.JPAQueryFactory

class MemberRepositoryImpl(
    private val jPAQueryFactory: JPAQueryFactory
) : MemberRepositoryCustom {

    override fun findMemberAndAuthority(username: String): MemberDto {

        return jPAQueryFactory
            .select(QMemberDto(
                member.loginId,
                member.name,
                member.loginPw,
                member.mobileNumber,
                member.id
            ))
            .from(member)
            .innerJoin(member.roles, memberAuthority)
            .where(member.loginId.eq(username))
            .fetchOne()!!
    }
}