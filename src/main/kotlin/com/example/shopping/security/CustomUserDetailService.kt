package com.example.shopping.security

import com.example.shopping.repository.MemberRepository
import org.springframework.security.core.CredentialsContainer
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailService(
    val memberRepository: MemberRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {

        val findMember = memberRepository.findByLoginId(username)

        val customUserDetails = CustomUserDetails(
            loginId = findMember.loginId,
            loginPw = findMember.loginPw!!,
            id = findMember.id,
            roles = findMember.roles!!.map { it.authority }.toMutableList()
        )

        return customUserDetails
    }

    class CustomUserDetails(
        val loginId: String,
        var loginPw: String?,
        val id: Long,
        val roles: MutableList<GrantedAuthority>?
    ) : UserDetails,
        CredentialsContainer {

        override fun getAuthorities(): MutableList<GrantedAuthority>? = null

        override fun getUsername(): String = this.loginId

        override fun getPassword(): String? = this.loginPw

        override fun eraseCredentials() {
            this.loginPw = null
        }
    }

}