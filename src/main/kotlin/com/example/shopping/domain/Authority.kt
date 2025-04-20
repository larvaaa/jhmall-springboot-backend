package com.example.shopping.domain

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority

@Entity
@Table(name = "authorities")
class Authority(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auth_id")
    val id: Long = 0,

    @Column(name = "auth_nm")
    var role: String

) : GrantedAuthority {

    override fun getAuthority(): String = this.role

}