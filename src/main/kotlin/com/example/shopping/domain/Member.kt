package com.example.shopping.domain

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority

@Entity
@Table(name = "members")
class Member(

    var loginId: String,

    var loginPw: String?,

    var name: String? = null,

    var mobileNumber: String? = null,

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @OneToMany
    var roles: MutableList<GrantedAuthority>? = null
) {

}