package com.example.shopping.service

import com.example.shopping.domain.Member
import com.example.shopping.domain.MemberAuthority
import com.example.shopping.repository.AuthorityRepository
import com.example.shopping.repository.MemberAuthorityRepository
import com.example.shopping.repository.MemberRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class MemberService(
    val memberRepository: MemberRepository,
    val memberAuthorityRepository: MemberAuthorityRepository,
    val authorityRepository: AuthorityRepository
) {

    private val log = LoggerFactory.getLogger(this.javaClass)!!

    fun findByLoginId(username: String): Member = memberRepository.findByLoginId(username)

    fun register(member: Member, role: String): Member {

        log.info("member save start")

        memberRepository.save(member)

        throw Exception("테스트용")

        val authority = authorityRepository.findByRole(role)
        val memberAuthority = MemberAuthority(member = member, authority = authority)
        memberAuthorityRepository.save(memberAuthority)

        log.info("member save result =>")
        log.info("==================")
        log.info("${member}")
        log.info("==================")

        return memberRepository.save(member)
    }

}