package com.example.shopping.service

import com.example.shopping.domain.Member
import com.example.shopping.repository.MemberRepository
import org.springframework.stereotype.Service

@Service
class MemberService(
    val memberRepository: MemberRepository
) {

    fun findByLoginId(username: String): Member = memberRepository.findByLoginId(username)

    fun register(member: Member): Member {
        return memberRepository.save(member)
    }

}